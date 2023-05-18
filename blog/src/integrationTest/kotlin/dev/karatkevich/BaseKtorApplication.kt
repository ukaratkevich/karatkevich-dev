package dev.karatkevich

import io.ktor.client.HttpClient
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json

internal fun withBaseApplication(
    configuration: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
) = testApplication {
    install(ContentNegotiation) {
        json(Json)
    }

    install(Resources)

    val client = createClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json)
        }
    }

    configuration(client)
}
