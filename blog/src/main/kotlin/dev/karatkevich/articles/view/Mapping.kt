package dev.karatkevich.articles.view

import dev.karatkevich.articles.domain.entities.Article

internal fun Article.toRepresentation(): ArticleRepresentation.Response {
    return ArticleRepresentation.Response(
        id = id.value,
        title = title,
        description = description,
        cover = cover,
        publishDate = publishDate.toString()
    )
}
