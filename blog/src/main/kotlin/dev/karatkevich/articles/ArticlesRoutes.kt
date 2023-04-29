package dev.karatkevich.articles

import dev.karatkevich.Blog.Articles
import dev.karatkevich.articles.models.Article
import dev.karatkevich.articles.models.toExisting
import dev.karatkevich.articles.services.ArticlesStore
import dev.karatkevich.articles.services.find
import dev.karatkevich.articles.validation.articleValidation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import java.util.UUID
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun Route.articlesRouting() {
    val articlesStore = ArticlesStore.InMemory

    articleValidation()

    get<Articles> {
        call.respond(articlesStore)
    }

    get<Articles.Id> { resource ->
        val article = articlesStore.find { article -> article.id == resource.id }

        call.respond(article ?: HttpStatusCode.NotFound)
    }

    post<Articles> {
        val article = call.receive<Article.New>()
        val id = UUID.randomUUID().toString()
        articlesStore.addArticle(article.toExisting(id))

        with(call) {
            response.headers.append(
                HttpHeaders.Location,
                Json.encodeToString(Articles.Id(id = id))
            )

            respond(HttpStatusCode.Created)
        }
    }

    put<Articles> {
        val article = call.receive<Article.Existing>()
        val isReplaced = articlesStore.replaceArticle(article)

        if (!isReplaced) {
            call.respond(HttpStatusCode.NotFound)
            return@put
        }

        call.respond(HttpStatusCode.OK)
    }

    delete<Articles.Id> { resource ->
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
