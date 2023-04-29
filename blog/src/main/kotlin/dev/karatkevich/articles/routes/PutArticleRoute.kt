package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.models.Article
import dev.karatkevich.articles.models.toExisting
import dev.karatkevich.articles.services.ArticlesStore
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.putArticleRoute(articlesStore: ArticlesStore) {
    put<Blog.Articles.Id> { resource ->
        val article = call.receive<Article.New>().toExisting(resource.id)
        val isReplaced = articlesStore.replaceArticle(article)

        if (!isReplaced) {
            call.respond(HttpStatusCode.NotFound)
            return@put
        }

        call.respond(HttpStatusCode.OK)
    }
}
