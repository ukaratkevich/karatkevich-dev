package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.toId
import dev.karatkevich.articles.view.ArticleRepresentation
import dev.karatkevich.articles.view.toRepresentation
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

internal fun Route.putArticleRoute(articlesRepository: ArticlesRepository) {
    put<Blog.Articles.Id> { resource ->
        val representation = call.receive<ArticleRepresentation.New>()

        val updateArticle = articlesRepository.save(
            Article(
                id = resource.id.toId(),
                title = representation.title,
                description = representation.description,
                cover = representation.cover,
            )
        )

        call.respond(updateArticle.toRepresentation())
    }
}
