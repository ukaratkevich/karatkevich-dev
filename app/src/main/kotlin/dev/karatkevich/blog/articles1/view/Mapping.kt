package dev.karatkevich.blog.articles1.view

import dev.karatkevich.blog.articles1.domain.entities.Article
import dev.karatkevich.blog.articles1.view.ArticleRepresentation.Response

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
