package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.toId
import dev.karatkevich.articles.view.Article
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import dev.karatkevich.articles.domain.entities.Article as DomainArticle

internal fun Route.putArticleRoute(articlesRepository: ArticlesRepository) {
    put<Blog.Articles.Id> { resource ->
        val article = call.receive<Article.New>()

        val updateArticle = articlesRepository.save(
            DomainArticle(
                id = resource.id.toId(),
                title = article.title,
                description = article.description,
                cover = article.cover,
            )
        )

        call.respond(updateArticle)
    }
}
