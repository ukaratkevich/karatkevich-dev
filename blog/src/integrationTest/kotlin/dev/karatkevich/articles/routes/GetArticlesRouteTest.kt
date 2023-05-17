package dev.karatkevich.articles.routes

import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.GetArticlesRouteTest.Environment.Companion.GET_PATH
import dev.karatkevich.articles.view.ArticleRepresentation
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.ktor.client.shouldHaveContentType
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlin.properties.Delegates
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

class GetArticlesRouteTest : DescribeSpec({
    var environment by Delegates.notNull<Environment>()

    beforeEach {
        environment = Environment()
    }

    describe("get request") {

        it("should return 200 OK with an empty list") {
            // TODO this probably may be done once
            testApplication {
                install(ContentNegotiation) {
                    json(Json)
                }

                install(Resources)

                routing {
                    getArticlesRoute(environment.articlesService)
                }

                createClient {
                    install(ClientContentNegotiation) {
                        json(Json)
                    }
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

        describe("new items has been added") {

            beforeEach {
                // TODO populate items
            }

            it("should return 200 OK with items") {
                testApplication {

                }
            }

            describe("get item by id") {

                it("should return 200 OK and the item") {
                    testApplication {

                    }
                }

                describe("item has been deleted") {

                    beforeEach {
                        // TODO remove item
                    }

                    describe("get item by id") {

                        it("should return 404 Not Found") {
                            testApplication {

                            }
                        }
                    }
                }
            }

            describe("items has been cleared") {

                beforeEach {
                    // TODO clear repo
                }

                it("should return 200 OK with an empty list") {
                    testApplication {

                    }
                }
            }
        }
    }
}) {
    private class Environment {
        val INSTANT = mockk<Instant> {
            every { this@mockk.toString() } returns "0000-00-00T00.00.000Z"
        }

        val articlesRepository = InMemoryArticlesRepository(
            UnconfinedTestDispatcher(),
            clock = mockk {
                every { now() } returns INSTANT
            },
            idGenerator = { "" }
        )
        val articlesService = ArticlesService(articlesRepository)

        companion object {
            const val GET_PATH = "v1/blog/articles"
        }
    }
}
