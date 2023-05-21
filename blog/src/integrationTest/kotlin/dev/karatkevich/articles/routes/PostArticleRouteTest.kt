package dev.karatkevich.articles.routes

import dev.karatkevich.articles.PATH
import dev.karatkevich.articles.articlesRoutes
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.withBaseApplication
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.client.request.accept
import io.ktor.client.request.post
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

class PostArticleRouteTest : DescribeSpec({

    var environment by Delegates.notNull<Environment>()

    beforeEach {
        environment = Environment()
    }

    describe("post article") {

        var response by Delegates.notNull<HttpResponse>()

        beforeEach {
            withBaseApplication(environment) { client ->
                response = client.post(PATH) {
                    accept(ContentType.Application.Json)
                    contentType(ContentType.Application.Json)
                    setBody(ARTICLE_REPRESENTATION)
                }
            }
        }

        it("returns 201 Created status code") {
            response.shouldHaveStatus(HttpStatusCode.Created)
        }

        it("should not return a payload") {
            response.bodyAsText().shouldBeEmpty()
        }

        it("returns location header") {
            with(response.headers["Location"]) {
                shouldNotBeNull()
                shouldNotBeBlank()
            }
        }

        it("should contain the article") {
            val location = response.headers["Location"].shouldNotBeNull()
            val uid = location.split('/').last()
            val article = environment.articlesRepository.getById(uid.toId())

            article.shouldNotBeNull()
        }

        it("should have the content of the representation") {
            val location = response.headers["Location"].shouldNotBeNull()
            val uid = location.split('/').last()

            with(environment.articlesRepository.getById(uid.toId())) {
                shouldNotBeNull()

                shouldBeEqualToIgnoringFields(
                    ARTICLE,
                    Article::uid,
                    Article::publishDate,
                    Article::updateDate,
                )
            }
        }

        describe("post the same article") {

            var secondResponse by Delegates.notNull<HttpResponse>()

            beforeEach {
                withBaseApplication(environment) { client ->
                    secondResponse = client.post(PATH) {
                        accept(ContentType.Application.Json)
                        contentType(ContentType.Application.Json)
                        setBody(ARTICLE_REPRESENTATION)
                    }
                }
            }

            it("returns 201 Created status code") {
                secondResponse.shouldHaveStatus(HttpStatusCode.Created)
            }

            it("should not return a payload") {
                secondResponse.bodyAsText().shouldBeEmpty()
            }

            it("returns location header") {
                with(secondResponse.headers["Location"]) {
                    shouldNotBeNull()
                    shouldNotBeBlank()
                }
            }

            it("should have the same id as in the header") {
                val location = secondResponse.headers["Location"].shouldNotBeNull()
                val uid = location.split('/').last()
                val article = environment.articlesRepository.getById(uid.toId())

                article.shouldNotBeNull()
            }

            it("should have the content of the representation") {
                val location = secondResponse.headers["Location"].shouldNotBeNull()
                val uid = location.split('/').last()

                with(environment.articlesRepository.getById(uid.toId())) {
                    shouldNotBeNull()

                    shouldBeEqualToIgnoringFields(
                        ARTICLE,
                        Article::uid,
                        Article::publishDate,
                        Article::updateDate,
                    )
                }
            }

            it("should not update the previous article") {
                val firstLocation = response.headers["Location"].shouldNotBeNull()
                val firstUid = firstLocation.split('/').last()

                val secondLocation = secondResponse.headers["Location"].shouldNotBeNull()
                val secondUid = secondLocation.split('/').last()

                val firstArticle =
                    environment.articlesRepository.getById(firstUid.toId()).shouldNotBeNull()
                val secondArticle =
                    environment.articlesRepository.getById(secondUid.toId()).shouldNotBeNull()

                firstArticle.shouldNotBeEqual(secondArticle)
            }
        }
    }

    // TODO add test cases here after validation constraints implementation
    describe("post invalid article") {

        beforeEach {

        }

        it("returns error") {

        }
    }
}) {
    private class Environment : (TestApplicationBuilder) -> Unit {

        val articlesRepository = InMemoryArticlesRepository(
            dispatcher = UnconfinedTestDispatcher(),
        )
        val articlesService = ArticlesService(articlesRepository)

        override fun invoke(builder: TestApplicationBuilder) = with(builder) {
            routing {
                articlesRoutes(articlesService)
            }
        }
    }

    private companion object {
        val ARTICLE_REPRESENTATION = ArticleRepresentation.Request(
            title = "0",
            description = "0",
            cover = "0",
        )

        val ARTICLE = Article(
            uid = "0".toId(),
            title = "0",
            description = "0",
            cover = "0",
            publishDate = Instant.fromEpochMilliseconds(0L),
        )
    }
}
