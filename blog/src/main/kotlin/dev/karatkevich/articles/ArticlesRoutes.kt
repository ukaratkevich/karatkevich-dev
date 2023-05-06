package dev.karatkevich.articles

import dev.karatkevich.articles.domain.validation.articleValidation
import dev.karatkevich.articles.model.InMemoryArticlesRepository
import dev.karatkevich.articles.routes.deleteArticleRoute
import dev.karatkevich.articles.routes.getArticlesRoute
import dev.karatkevich.articles.routes.postArticleRoute
import dev.karatkevich.articles.routes.putArticleRoute
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import java.util.UUID

internal fun Application.articlesRouting() {
    routing {
        val articlesRepository = InMemoryArticlesRepository(
            idGenerator = { UUID.randomUUID().toString() }
        )

        articleValidation()

        getArticlesRoute(articlesRepository)
        postArticleRoute(articlesRepository)
        putArticleRoute(articlesRepository)
        deleteArticleRoute(articlesRepository)
    }
}
