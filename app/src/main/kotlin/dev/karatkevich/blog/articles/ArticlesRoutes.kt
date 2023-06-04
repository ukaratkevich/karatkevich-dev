package dev.karatkevich.blog.articles

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles.domain.ArticlesService
import dev.karatkevich.blog.articles.routes.deleteArticleRoute
import dev.karatkevich.blog.articles.routes.getArticlesRoute
import dev.karatkevich.blog.articles.routes.postArticleRoute
import dev.karatkevich.blog.articles.routes.putArticleRoute
import dev.karatkevich.blog.articles.view.validation.installArticlesValidation
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
