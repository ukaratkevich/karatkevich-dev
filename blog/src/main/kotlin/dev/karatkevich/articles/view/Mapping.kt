package dev.karatkevich.articles.view

import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.view.ArticleRepresentation.Existing

internal fun Article.toRepresentation(): Existing {
    return Existing(
        id = id.value,
        title = title,
        description = description,
        cover = cover,
    )
}
