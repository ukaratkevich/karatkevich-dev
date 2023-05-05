package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Id
import dev.karatkevich.articles.domain.entities.toId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.getArticlesRoute(articlesRepository: ArticlesRepository) {
    get<Blog.Articles> {
        call.respond(articlesRepository.getAll())
    }

    get<Blog.Articles.Id> { resource ->
        val article = articlesRepository.getById(resource.id.toId())

        call.respond(article ?: HttpStatusCode.NotFound)
    }
}
