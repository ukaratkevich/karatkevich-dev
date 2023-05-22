package dev.karatkevich.articles

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesService
import dev.karatkevich.articles.routes.deleteArticleRoute
import dev.karatkevich.articles.routes.getArticlesRoute
import dev.karatkevich.articles.routes.postArticleRoute
import dev.karatkevich.articles.routes.putArticleRoute
import dev.karatkevich.articles.view.validation.installArticlesValidation
import io.ktor.server.resources.resource
import io.ktor.server.routing.Route

fun Route.articlesRoutes(
    articlesService: ArticlesService,
) {
    resource<Blog.Articles> {
        installArticlesValidation()
    }

    getArticlesRoute(articlesService)
    postArticleRoute(articlesService)
    putArticleRoute(articlesService)
    deleteArticleRoute(articlesService)
}
