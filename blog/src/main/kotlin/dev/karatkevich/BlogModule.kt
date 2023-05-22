@file:JvmName("BlogModule")

package dev.karatkevich

import dev.karatkevich.articles.articlesRoutes
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.blogModule() {
    val articlesService = ArticlesService(
        repository = InMemoryArticlesRepository(),
    )

    routing {
        articlesRoutes(articlesService)
    }
}
