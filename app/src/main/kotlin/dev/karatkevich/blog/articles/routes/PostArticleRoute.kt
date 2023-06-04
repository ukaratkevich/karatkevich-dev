package dev.karatkevich.blog.articles.routes

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.domain.entities.Id
import dev.karatkevich.blog.articles.view.ArticleRepresentation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.href
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.postArticleRoute(articlesService: ArticlesService) {
    post<Blog.Articles> {
        val representation = call.receive<ArticleRepresentation.Request>()

        val createdArticle = articlesService.create(
            Article(
                uid = Id.EMPTY,
                title = representation.title,
                description = representation.description,
                cover = representation.cover,
            )
        )

        with(call) {
            response.headers.append(
                HttpHeaders.Location,
                application.href(Blog.Articles.Id(id = createdArticle.uid.value))
            )

            respond(HttpStatusCode.Created)
        }
    }
}
