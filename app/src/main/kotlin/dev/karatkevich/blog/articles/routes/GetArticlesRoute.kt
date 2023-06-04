package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.blog.articles.view.toRepresentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.getArticlesRoute(articlesService: ArticlesService) {
    get<Blog.Articles> {
        call.respond(articlesService.getAll().map(Article::toRepresentation))
    }

    get<Blog.Articles.Id> { resource ->
        val article = articlesService.getById(resource.id.toId())

        if (article != null) {
            call.respond(article.toRepresentation())
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}