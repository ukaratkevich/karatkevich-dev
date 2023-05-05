package dev.karatkevich.articles.routes

import dev.karatkevich.Blog
import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.toId
import dev.karatkevich.articles.view.Article
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.href
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import java.util.UUID
import dev.karatkevich.articles.domain.entities.Article as DomainArticle

internal fun Route.postArticleRoute(articlesRepository: ArticlesRepository) {
    post<Blog.Articles> {
        val article = call.receive<Article.New>()

        val createArticle = articlesRepository.save(
            DomainArticle(
                id = UUID.randomUUID().toString().toId(),
                title = article.title,
                description = article.description,
                cover = article.cover,
            )
        )

        with(call) {
            response.headers.append(
                HttpHeaders.Location,
                application.href(Blog.Articles.Id(id = createArticle.id.value))
            )

            respond(HttpStatusCode.Created)
        }
    }
}
