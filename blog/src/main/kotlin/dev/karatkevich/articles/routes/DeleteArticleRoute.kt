package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.deleteArticleRoute(articlesRepository: ArticlesRepository) {
    delete<Blog.Articles.Id> { resource ->
        val article = articlesRepository.delete(resource.id.toId())

        val code = if (article == null) {
            HttpStatusCode.NotFound
        } else {
            HttpStatusCode.NoContent
        }

        call.respond(code)
    }
}
