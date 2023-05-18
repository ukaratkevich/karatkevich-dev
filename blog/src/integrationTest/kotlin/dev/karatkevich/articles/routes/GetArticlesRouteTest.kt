package dev.karatkevich.articles.routes

import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLES
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLES_REPRESENTATION
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.GET_PATH
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.withBaseApplication
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.ktor.client.shouldHaveContentType
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldBeEmpty
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.mockk.every
import io.mockk.mockk
import kotlin.properties.Delegates
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Instant

class GetArticlesRouteTest : DescribeSpec({
    var env by Delegates.notNull<Environment>()

    beforeEach {
        env = Environment()
    }

    describe("get request") {

        it("should return 200 OK with no articles") {
            withBaseApplication { client ->
                routing {
                    getArticlesRoute(env.articlesService)
                }

                val response = client.get(GET_PATH)
                assertSoftly {
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    response.shouldHaveContentType(
                        ContentType.Application.Json.withCharset(Charsets.UTF_8)
                    )
                    response.body<List<ArticleRepresentation.Response>>().shouldBeEmpty()
                }
            }
        }

        describe("new articles has been added") {

            beforeEach {
                ARTICLES.forEach { article ->
                    env.articlesRepository.save(article)
                }
            }

            it("should return 200 OK with articles") {
                withBaseApplication { client ->
                    routing {
                        getArticlesRoute(env.articlesService)
                    }

                    val response = client.get(GET_PATH)
                    assertSoftly {
                        response.shouldHaveStatus(HttpStatusCode.OK)
                        response.shouldHaveContentType(
                            ContentType.Application.Json.withCharset(Charsets.UTF_8)
                        )
                        response.body<List<ArticleRepresentation.Response>>()
                            .shouldContainExactly(ARTICLES_REPRESENTATION)
                    }
                }
            }

            describe("get article by id") {

                it("should return 200 OK and the article") {
                    withBaseApplication { client ->
                        routing {
                            getArticlesRoute(env.articlesService)
                        }

                        val response = client.get("$GET_PATH/0")
                        assertSoftly {
                            response.shouldHaveStatus(HttpStatusCode.OK)
                            response.shouldHaveContentType(
                                ContentType.Application.Json.withCharset(Charsets.UTF_8)
                            )
                            response.body<ArticleRepresentation.Response>()
                                .shouldBeEqual(ARTICLES_REPRESENTATION.first())
                        }
                    }
                }

                describe("an article has been deleted") {

                    beforeEach {
                        env.articlesRepository.delete("0".toId())
                    }

                    describe("get item by id") {

                        it("should return 404 Not Found") {
                            withBaseApplication { client ->
                                routing {
                                    getArticlesRoute(env.articlesService)
                                }

                                val response = client.get("$GET_PATH/0")
                                assertSoftly {
                                    response.shouldHaveStatus(HttpStatusCode.NotFound)
                                    response.bodyAsText().shouldBeEmpty()
                                }
                            }
                        }
                    }

                    describe("get articles") {

                        it("should return 200 OK and articles without deleted one") {
                            withBaseApplication { client ->
                                routing {
                                    getArticlesRoute(env.articlesService)
                                }

                                val response = client.get(GET_PATH)
                                assertSoftly {
                                    response.shouldHaveStatus(HttpStatusCode.OK)
                                    response.shouldHaveContentType(
                                        ContentType.Application.Json.withCharset(Charsets.UTF_8)
                                    )
                                    response.body<List<ArticleRepresentation.Response>>()
                                        .shouldContainExactly(ARTICLES_REPRESENTATION.drop(1))
                                }
                            }
                        }
                    }
                }
            }

            describe("articles has been cleared") {

                beforeEach {
                    ARTICLES.forEach { article ->
                        env.articlesRepository.delete(article.uid)
                    }
                }

                it("should return 200 OK with no articles") {
                    withBaseApplication { client ->
                        routing {
                            getArticlesRoute(env.articlesService)
                        }

                        val response = client.get(GET_PATH)
                        assertSoftly {
                            response.shouldHaveStatus(HttpStatusCode.OK)
                            response.shouldHaveContentType(
                                ContentType.Application.Json.withCharset(Charsets.UTF_8)
                            )
                            response.body<List<ArticleRepresentation.Response>>().shouldBeEmpty()
                        }
                    }
                }
            }
        }
    }
}) {
    private class Environment {
        val articlesRepository = InMemoryArticlesRepository(
            dispatcher = UnconfinedTestDispatcher(),
            clock = mockk {
                every { now() } returns INSTANT
            },
            idGenerator = { "" }
        )
        val articlesService = ArticlesService(articlesRepository)

        companion object {
            const val GET_PATH = "v1/blog/articles"
            const val UTC_DATE = "0000-00-00T00.00.000Z"

            val INSTANT = mockk<Instant> {
                every { this@mockk.toString() } returns UTC_DATE
            }

            val ARTICLES = Array(10) { id ->
                Article(
                    uid = "$id".toId(),
                    title = "$id",
                    description = null,
                    cover = null,
                    publishDate = INSTANT,
                    updateDate = INSTANT,
                )
            }

            val ARTICLES_REPRESENTATION = ARTICLES.map { article ->
                ArticleRepresentation.Response(
                    uid = article.uid.value,
                    title = article.title,
                    description = article.description,
                    cover = article.cover,
                    published = UTC_DATE,
                    updated = UTC_DATE,
                )
            }
        }
    }
}
