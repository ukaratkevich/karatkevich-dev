package dev.karatkevich.blog.articles.view

import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.view.ArticleRepresentation.Response

internal fun Article.toRepresentation(): Response {
    return Response(
        uid = uid.value,
        title = title,
        description = description,
        cover = cover,
        published = publishDate.toString(),
        updated = updateDate.toString(),
    )
}
