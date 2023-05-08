package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.view.toRepresentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.getArticlesRoute(articlesRepository: ArticlesRepository) {
    get<Blog.Articles> {
        call.respond(articlesRepository.getAll().map(Article::toRepresentation))
    }

    get<Blog.Articles.Id> { resource ->
        val article = articlesRepository.getById(resource.id.toId())

        call.respond(article?.toRepresentation() ?: HttpStatusCode.NotFound)
    }
}
