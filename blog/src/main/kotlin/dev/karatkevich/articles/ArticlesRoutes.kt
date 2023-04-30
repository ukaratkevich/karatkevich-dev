package dev.karatkevich.articles

import dev.karatkevich.articles.routes.deleteArticleRoute
import dev.karatkevich.articles.routes.getArticlesRoute
import dev.karatkevich.articles.routes.postArticleRoute
import dev.karatkevich.articles.routes.putArticleRoute
import dev.karatkevich.articles.services.ArticlesStore
import dev.karatkevich.articles.validation.articleValidation
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

internal fun Application.articlesRouting() {
    routing {
        val articlesStore = ArticlesStore.InMemory

        articleValidation()

        getArticlesRoute(articlesStore)
        postArticleRoute(articlesStore)
        putArticleRoute(articlesStore)
        deleteArticleRoute(articlesStore)
    }
}
