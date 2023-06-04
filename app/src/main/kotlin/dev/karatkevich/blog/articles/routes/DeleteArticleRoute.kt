package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.blog.articles.view.toRepresentation
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
