package dev.karatkevich.blog.articles1

import dev.karatkevich.blog.Blog
import dev.karatkevich.blog.articles1.domain.ArticlesService
import dev.karatkevich.blog.articles1.routes.deleteArticleRoute
import dev.karatkevich.blog.articles1.routes.getArticlesRoute
import dev.karatkevich.blog.articles1.routes.postArticleRoute
import dev.karatkevich.blog.articles1.routes.putArticleRoute
import dev.karatkevich.blog.articles1.view.validation.installArticlesValidation
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
