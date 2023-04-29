@file:JvmName("BlogModule")

package dev.karatkevich

import dev.karatkevich.articles.articlesRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.blogModule() {
    routing {
        articlesRouting()
    }
}
