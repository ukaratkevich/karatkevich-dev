package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.services.ArticlesStore
import dev.karatkevich.articles.services.find
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.getArticlesRoute(articlesStore: ArticlesStore) {
    get<Blog.Articles> {
        call.respond(articlesStore.getArticles())
    }

    get<Blog.Articles.Id> { resource ->
        val article = articlesStore.find { article -> article.id == resource.id }

        call.respond(article ?: HttpStatusCode.NotFound)
    }
}
