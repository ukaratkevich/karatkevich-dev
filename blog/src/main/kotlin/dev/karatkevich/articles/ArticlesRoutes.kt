package dev.karatkevich.articles

import dev.karatkevich.articles.domain.validation.articleValidation
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.deleteArticleRoute
import dev.karatkevich.articles.routes.getArticlesRoute
import dev.karatkevich.articles.routes.postArticleRoute
import dev.karatkevich.articles.routes.putArticleRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers

internal fun Application.articlesRouting() {
    routing {
        val articlesRepository = InMemoryArticlesRepository(Dispatchers.IO)

        articleValidation()

        getArticlesRoute(articlesRepository)
        postArticleRoute(articlesRepository)
        putArticleRoute(articlesRepository)
        deleteArticleRoute(articlesRepository)
    }
}
