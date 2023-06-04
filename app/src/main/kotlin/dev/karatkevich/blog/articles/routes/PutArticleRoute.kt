package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.blog.articles.view.ArticleRepresentation
import dev.karatkevich.blog.articles.view.toRepresentation
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
