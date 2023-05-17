package dev.karatkevich.articles.view

import dev.karatkevich.articles.domain.entities.Article

internal fun Article.toRepresentation(): ArticleRepresentation.Response {
    return ArticleRepresentation.Response(
        id = uid.value,
        title = title,
        description = description,
        cover = cover,
        published = publishDate.toString(),
        updated = updateDate.toString(),
    )
}
