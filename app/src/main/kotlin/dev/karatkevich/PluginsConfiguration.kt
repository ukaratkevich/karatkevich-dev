@file:JvmName("PluginsConfiguration")

package dev.karatkevich

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import kotlinx.serialization.json.Json

fun Application.configurePlugins() {
    install(Resources)

    install(ContentNegotiation) {
        json(
            json = Json
        )
    }
}
