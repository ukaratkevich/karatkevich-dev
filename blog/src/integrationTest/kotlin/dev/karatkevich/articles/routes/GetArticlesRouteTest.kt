package dev.karatkevich.articles.routes

import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.ARTICLES_REPRESENTATION
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.PATH
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.withBaseApplication
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.ktor.client.shouldHaveContentType
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldBeEmpty
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.TestApplicationBuilder
import kotlin.properties.Delegates
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Instant

class GetArticlesRouteTest : DescribeSpec({

    var environment by Delegates.notNull<Environment>()

    describe("get request with empty articles") {

        beforeEach {
            environment = Environment(emptyList())
        }

        it("should return 200 OK with no articles") {
            withBaseApplication(environment) { client ->
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
            environment = Environment()
        }

        it("should return 200 OK with articles") {
            withBaseApplication(environment) { client ->
                val response = client.get(PATH)

                assertSoftly {
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    response.shouldHaveContentType(
                        ContentType.Application.Json.withCharset(Charsets.UTF_8)
                    )

                    val body = response.body<List<ArticleRepresentation.Response>>()
                    body.shouldContainExactly(ARTICLES_REPRESENTATION)
                    body.shouldBeSortedBy { Instant.parse(it.published) }
                }
            }
        }

        describe("get article with valid uid") {

            ARTICLES_REPRESENTATION.forEach { representation ->
                it("should return 200 OK and $representation") {
                    withBaseApplication(environment) { client ->
                        val response = client.get("$PATH/${representation.uid}")

                        assertSoftly {
                            response.shouldHaveStatus(HttpStatusCode.OK)
                            response.shouldHaveContentType(
                                ContentType.Application.Json.withCharset(Charsets.UTF_8)
                            )
                            response.body<ArticleRepresentation.Response>()
                                .shouldBeEqual(representation)
                        }
                    }
                }
            }
        }

        describe("get article with invalid uid") {

            it("should return 404 Not Found and empty body") {
                withBaseApplication(environment) { client ->
                    val response = client.get("$PATH/invalid_id")

                    assertSoftly {
                        response.shouldHaveStatus(HttpStatusCode.NotFound)
                        response.bodyAsText().shouldBeEmpty()
                    }
                }
            }
        }
    }
}) {
    private class Environment(
        articles: List<Article> = ARTICLES,
    ) : (TestApplicationBuilder) -> Unit {

        val articlesRepository = InMemoryArticlesRepository(
            dispatcher = UnconfinedTestDispatcher(),
            initial = articles,
        )
        val articlesService = ArticlesService(articlesRepository)

        override fun invoke(builder: TestApplicationBuilder) = with(builder) {
            routing {
                getArticlesRoute(articlesService)
            }
        }

        companion object {
            const val PATH = "v1/blog/articles"

            val ARTICLES = List(10) { id ->
                Article(
                    uid = "$id".toId(),
                    title = "$id",
                    description = null,
                    cover = null,
                    publishDate = Instant.fromEpochMilliseconds(id.toLong()),
                )
            }

            val ARTICLES_REPRESENTATION = ARTICLES
                .sortedBy(Article::publishDate)
                .map { article ->
                    ArticleRepresentation.Response(
                        uid = article.uid.value,
                        title = article.title,
                        description = article.description,
                        cover = article.cover,
                        published = article.publishDate.toString(),
                        updated = article.updateDate.toString(),
                    )
                }
        }
    }
}
