@file:JvmName("BlogModule")

package dev.karatkevich

import dev.karatkevich.articles.articlesRouting
import io.ktor.server.application.Application

fun Application.blogModule() {
    articlesRouting()
}
