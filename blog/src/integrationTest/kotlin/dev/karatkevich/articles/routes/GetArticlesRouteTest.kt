package dev.karatkevich.articles.routes

import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLES
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLES_REPRESENTATIONS
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLE_WITH_INVALID_UID
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLE_WITH_VALID_UID
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.PATH
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

    describe("get request with empty articles") {

        it("should return 200 OK with no articles") {
            withBaseApplication { client ->
                routing {
                    getArticlesRoute(env.articlesService)
                }

                val response = client.get(PATH)
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

    describe("get with populated articles") {

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

                val response = client.get(PATH)
                assertSoftly {
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    response.shouldHaveContentType(
                        ContentType.Application.Json.withCharset(Charsets.UTF_8)
                    )
                    response.body<List<ArticleRepresentation.Response>>()
                        .shouldContainExactly(ARTICLES_REPRESENTATIONS)
                }
            }
        }

        describe("get article with valid uid=${ARTICLE_WITH_VALID_UID.uid}") {
            it("should return 200 OK and $ARTICLE_WITH_VALID_UID") {
                withBaseApplication { client ->
                    routing {
                        getArticlesRoute(env.articlesService)
                    }

                    val response = client.get("$PATH/${ARTICLE_WITH_VALID_UID.uid}")
                    assertSoftly {
                        response.shouldHaveStatus(HttpStatusCode.OK)
                        response.shouldHaveContentType(
                            ContentType.Application.Json.withCharset(Charsets.UTF_8)
                        )
                        response.body<ArticleRepresentation.Response>()
                            .shouldBeEqual(ARTICLE_WITH_VALID_UID)
                    }
                }
            }
        }

        describe("get article with invalid uid=${ARTICLE_WITH_INVALID_UID.uid}") {
            it("should return 404 Not Found and empty body") {
                withBaseApplication { client ->
                    routing {
                        getArticlesRoute(env.articlesService)
                    }

                    val response = client.get("$PATH/${ARTICLE_WITH_INVALID_UID.uid}")
                    assertSoftly {
                        response.shouldHaveStatus(HttpStatusCode.NotFound)
                        response.bodyAsText().shouldBeEmpty()
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
            const val PATH = "v1/blog/articles"
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

            val ARTICLES_REPRESENTATIONS = ARTICLES.map { article ->
                ArticleRepresentation.Response(
                    uid = article.uid.value,
                    title = article.title,
                    description = article.description,
                    cover = article.cover,
                    published = UTC_DATE,
                    updated = UTC_DATE,
                )
            }

            val ARTICLE_WITH_VALID_UID = ARTICLES_REPRESENTATIONS.first()

            val ARTICLE_WITH_INVALID_UID = ARTICLE_WITH_VALID_UID.copy(
                uid = ARTICLE_WITH_VALID_UID.uid.repeat(5)
            )
        }
    }
}
