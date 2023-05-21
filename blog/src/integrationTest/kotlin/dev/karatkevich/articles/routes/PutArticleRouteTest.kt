package dev.karatkevich.articles.routes

import dev.karatkevich.articles.ARTICLES_PATH
import dev.karatkevich.articles.articlesRoutes
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.withBaseApplication
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.string.shouldBeEmpty
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.TestApplicationBuilder
import kotlin.properties.Delegates
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Instant

class PutArticleRouteTest : DescribeSpec({

    describe("PUT existing article") {
        var environment by Delegates.notNull<Environment>()
        var response by Delegates.notNull<HttpResponse>()

        beforeEach {
            environment = Environment(ARTICLE)

            withBaseApplication(environment) { client ->
                response = client.put("$ARTICLES_PATH/${ARTICLE.uid.value}") {
                    accept(ContentType.Application.Json)
                    contentType(ContentType.Application.Json)
                    setBody(ARTICLE_REPRESENTATION)
                }
            }
        }

        it("should return 200 OK") {
            response.shouldHaveStatus(HttpStatusCode.OK)
        }

        it("should return updated article as a payload") {
            val updatedArticle = response.body<ArticleRepresentation.Response>()

            updatedArticle.shouldBeEqualToIgnoringFields(
                ARTICLE_RESPONSE,
                ArticleRepresentation.Response::updated,
            )
        }

        it("should have new updated date") {
            val updatedArticle = response.body<ArticleRepresentation.Response>()

            val oldUpdatedDate = ARTICLE.updateDate
            val newUpdatedDate = Instant.parse(updatedArticle.updated)

            newUpdatedDate.shouldBeGreaterThan(oldUpdatedDate)
        }
    }

    describe("PUT a new article") {
        var environment by Delegates.notNull<Environment>()
        var response by Delegates.notNull<HttpResponse>()

        beforeEach {
            environment = Environment()

            withBaseApplication(environment) { client ->
                response = client.put("$ARTICLES_PATH/-1") {
                    accept(ContentType.Application.Json)
                    contentType(ContentType.Application.Json)
                    setBody(ARTICLE_REPRESENTATION)
                }
            }
        }

        it("should return 404 Not Found") {
            response.shouldHaveStatus(HttpStatusCode.NotFound)
        }

        it("should not have a payload") {
            response.bodyAsText().shouldBeEmpty()
        }
    }
}) {
    private class Environment(
        article: Article = ARTICLE,
    ) : (TestApplicationBuilder) -> Unit {
        val articlesRepository = InMemoryArticlesRepository(
            dispatcher = UnconfinedTestDispatcher(),
            initial = listOf(article),
        )
        val articlesService = ArticlesService(articlesRepository)

        override fun invoke(builder: TestApplicationBuilder) {
            with(builder) {
                routing {
                    articlesRoutes(articlesService)
                }
            }
        }
    }

    private companion object {
        val ARTICLE_REPRESENTATION = ArticleRepresentation.Request(
            title = "1",
            description = "1",
            cover = "1",
        )

        val ARTICLE = Article(
            uid = "0".toId(),
            title = "0",
            description = "0",
            cover = "0",
            publishDate = Instant.fromEpochMilliseconds(0L),
        )

        val ARTICLE_RESPONSE = ArticleRepresentation.Response(
            uid = "0",
            title = "1",
            description = "1",
            cover = "1",
            published = "1970-01-01T00:00:00Z",
            updated = "1970-01-01T00:00:00Z",
        )
    }
}
