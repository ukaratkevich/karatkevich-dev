package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.view.toRepresentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.deleteArticleRoute(articlesService: ArticlesService) {
    delete<Blog.Articles.Id> { resource ->
        val article = articlesService.delete(resource.id.toId())

        if (article == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(article.toRepresentation())
        }
    }
}
