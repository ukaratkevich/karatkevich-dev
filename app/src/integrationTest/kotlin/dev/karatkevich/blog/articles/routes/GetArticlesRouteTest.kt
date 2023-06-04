package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.articles.ARTICLES_PATH
import dev.karatkevich.blog.articles1.articlesRoutes
import dev.karatkevich.blog.articles1.domain.ArticlesService
import dev.karatkevich.blog.articles1.domain.entities.Article
import dev.karatkevich.blog.articles1.domain.entities.Id.Companion.toId
import dev.karatkevich.blog.articles1.model.InMemoryArticlesRepository
import dev.karatkevich.blog.articles1.view.ArticleRepresentation
import dev.karatkevich.blog.articles1.view.ArticleRepresentation.Response
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
import io.ktor.http.ContentType.Application
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
                val response = client.get(ARTICLES_PATH)

                assertSoftly {
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    response.shouldHaveContentType(
                        Application.Json.withCharset(Charsets.UTF_8)
                    )
                    response.body<List<Response>>().shouldBeEmpty()
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
                val response = client.get(ARTICLES_PATH)

                assertSoftly {
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    response.shouldHaveContentType(
                        Application.Json.withCharset(Charsets.UTF_8)
                    )

                    val body = response.body<List<Response>>()
                    body.shouldContainExactly(ARTICLES_REPRESENTATION)
                    body.shouldBeSortedBy { Instant.parse(it.published) }
                }
            }
        }

        describe("get article with valid uid") {

            ARTICLES_REPRESENTATION.forEach { representation ->
                it("should return 200 OK and $representation") {
                    withBaseApplication(environment) { client ->
                        val response = client.get("$ARTICLES_PATH/${representation.uid}")

                        assertSoftly {
                            response.shouldHaveStatus(HttpStatusCode.OK)
                            response.shouldHaveContentType(
                                Application.Json.withCharset(Charsets.UTF_8)
                            )
                            response.body<Response>()
                                .shouldBeEqual(representation)
                        }
                    }
                }
            }
        }

        describe("get article with invalid uid") {

            it("should return 404 Not Found and empty body") {
                withBaseApplication(environment) { client ->
                    val response = client.get("$ARTICLES_PATH/invalid_id")

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
                articlesRoutes(articlesService)
            }
        }
    }

    private companion object {
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
