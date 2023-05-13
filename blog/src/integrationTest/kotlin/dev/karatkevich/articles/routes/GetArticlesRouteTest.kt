package dev.karatkevich.articles.routes

import dev.karatkevich.articles.model.InMemoryArticlesRepository
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.server.testing.testApplication
import kotlin.properties.Delegates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class GetArticlesRouteTest : DescribeSpec({
    var environment by Delegates.notNull<Environment>()

    beforeEach {
        environment = Environment()
    }

    describe("get request") {

        it("should return 200 OK with an empty list") {
            testApplication {

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
    private class Environment(
        coroutineDispatcher: CoroutineDispatcher = StandardTestDispatcher(),
    ) {
        val articlesRepository = Any()

        init {
            InMemoryArticlesRepository()
        }
    }
}
