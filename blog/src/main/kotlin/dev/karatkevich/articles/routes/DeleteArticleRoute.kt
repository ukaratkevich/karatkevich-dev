package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.toId
import dev.karatkevich.articles.view.toRepresentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.deleteArticleRoute(articlesRepository: ArticlesRepository) {
    delete<Blog.Articles.Id> { resource ->
        val article = articlesRepository.delete(resource.id.toId())

        call.respond(article?.toRepresentation() ?: HttpStatusCode.NotFound)
    }
}
