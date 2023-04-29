package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.services.ArticlesStore
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.deleteArticleRoute(articlesStore: ArticlesStore) {
    delete<Blog.Articles.Id> { resource ->
        val isRemoved = articlesStore.removeArticle(resource.id)

        val code = if (isRemoved) {
            // return representation?
            HttpStatusCode.NoContent
        } else {
            HttpStatusCode.NotFound
        }

        call.respond(code)
    }
}
