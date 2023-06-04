package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.articles.ARTICLES_PATH
import dev.karatkevich.blog.articles.articlesRoutes
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.blog.articles.model.InMemoryArticlesRepository
import dev.karatkevich.blog.articles.view.ArticleRepresentation
import dev.karatkevich.withBaseApplication
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldBeEmpty
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationBuilder
import kotlin.properties.Delegates
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Instant

class DeleteArticleRouteTest : DescribeSpec({

    describe("DELETE article") {
        var environment by Delegates.notNull<Environment>()
        var response by Delegates.notNull<HttpResponse>()

        beforeEach {
            environment = Environment(listOf(ARTICLE))

            withBaseApplication(environment) { client ->
                response = client.delete("$ARTICLES_PATH/${ARTICLE.uid.value}") {
                    accept(Application.Json)
                }
            }
        }

        it("should return 200 OK status code") {
            response.shouldHaveStatus(HttpStatusCode.OK)
        }

        it("should return deleted article as a payload") {
            val deletedArticle = response.body<ArticleRepresentation.Response>()

            deletedArticle.shouldBeEqual(DELETED_ARTICLE_RESPONSE)
        }
    }

    describe("DELETE not existing article") {
        var environment by Delegates.notNull<Environment>()
        var response by Delegates.notNull<HttpResponse>()

        beforeEach {
            environment = Environment(emptyList())

            withBaseApplication(environment) { client ->
                response = client.delete("$ARTICLES_PATH/${ARTICLE.uid.value}") {
                    accept(Application.Json)
                }
            }
        }

        it("should return 404 Not Found status code") {
            response.shouldHaveStatus(HttpStatusCode.NotFound)
        }

        it("should not have a payload") {
            response.bodyAsText().shouldBeEmpty()
        }
    }
}) {
    private class Environment(
        articles: List<Article>,
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
        val ARTICLE = Article(
            uid = "0".toId(),
            title = "0",
            description = "0",
            cover = "0",
            publishDate = Instant.fromEpochMilliseconds(0L),
        )

        val DELETED_ARTICLE_RESPONSE = ArticleRepresentation.Response(
            uid = "0",
            title = "0",
            description = "0",
            cover = "0",
            published = "1970-01-01T00:00:00Z",
            updated = "1970-01-01T00:00:00Z",
        )
    }
}
