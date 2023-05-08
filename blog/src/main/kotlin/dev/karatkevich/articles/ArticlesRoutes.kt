package dev.karatkevich.articles

import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.domain.validation.articleValidation
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.deleteArticleRoute
import dev.karatkevich.articles.routes.getArticlesRoute
import dev.karatkevich.articles.routes.postArticleRoute
import dev.karatkevich.articles.routes.putArticleRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

internal fun Application.articlesRouting() {
    routing {
        val articlesService = ArticlesService(
            repository = InMemoryArticlesRepository(),
        )

        articleValidation()

        getArticlesRoute(articlesService)
        postArticleRoute(articlesService)
        putArticleRoute(articlesService)
        deleteArticleRoute(articlesService)
    }
}
