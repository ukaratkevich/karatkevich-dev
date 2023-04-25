package dev.karatkevich.articles

import dev.karatkevich.Blog.Articles
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.articlesRouting() {
    val articles = mutableListOf<Article>()

    get<Articles> {
        call.respond(articles)
    }

    get<Articles.Id> {
        call.respond(HttpStatusCode.InternalServerError)
    }

    post<Articles> {
        val article = call.receive<Article>()
        articles += article
        call.respond(HttpStatusCode.Created)
    }

    put<Articles.Id> {
        call.respond(HttpStatusCode.InternalServerError)
    }

    delete<Articles.Id> {
        call.respond(HttpStatusCode.InternalServerError)
    }
}
