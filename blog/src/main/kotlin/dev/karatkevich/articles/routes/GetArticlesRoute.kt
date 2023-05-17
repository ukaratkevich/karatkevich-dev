package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.view.toRepresentation
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

        call.respond(article?.toRepresentation() ?: HttpStatusCode.NotFound)
    }
}
