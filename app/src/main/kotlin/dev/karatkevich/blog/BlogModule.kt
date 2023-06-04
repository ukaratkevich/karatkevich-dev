@file:JvmName("BlogModule")

package dev.karatkevich.blog

import dev.karatkevich.blog.articles.articlesRoutes
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.model.InMemoryArticlesRepository
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
