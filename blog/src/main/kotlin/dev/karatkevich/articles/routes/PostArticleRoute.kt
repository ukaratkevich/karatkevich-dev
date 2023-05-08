package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id
import dev.karatkevich.articles.view.ArticleRepresentation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.href
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.postArticleRoute(articlesService: ArticlesService) {
    post<Blog.Articles> {
        val representation = call.receive<ArticleRepresentation.Request>()

        val createdArticle = articlesService.create(
            Article(
                id = Id.EMPTY,
                title = representation.title,
                description = representation.description,
                cover = representation.cover,
            )
        )

        with(call) {
            response.headers.append(
                HttpHeaders.Location,
                application.href(Blog.Articles.Id(id = createdArticle.id.value))
            )

            respond(HttpStatusCode.Created)
        }
    }
}
