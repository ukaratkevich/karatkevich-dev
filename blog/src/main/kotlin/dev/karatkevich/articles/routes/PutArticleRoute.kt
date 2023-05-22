package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.articles.view.toRepresentation
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.putArticleRoute(articlesService: ArticlesService) {
    put<Blog.Articles.Id> { resource ->
        val representation = call.receive<ArticleRepresentation.Request>()

        val updateArticle = articlesService.update(
            Article(
                uid = resource.id.toId(),
                title = representation.title,
                description = representation.description,
                cover = representation.cover,
            )
        )

        if (updateArticle == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(updateArticle.toRepresentation())
        }
    }
}
