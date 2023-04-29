package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.models.Article
import dev.karatkevich.articles.models.toExisting
import dev.karatkevich.articles.services.ArticlesStore
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.href
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import java.util.UUID

internal fun Route.postArticleRoute(articlesStore: ArticlesStore) {
    post<Blog.Articles> {
        val article = call.receive<Article.New>()

        val id = UUID.randomUUID().toString()
        articlesStore.addArticle(article.toExisting(id))

        with(call) {
            response.headers.append(
                HttpHeaders.Location,
                application.href(Blog.Articles.Id(id = id))
            )

            respond(HttpStatusCode.Created)
        }
    }
}
