package dev.karatkevich.articles.domain.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Article(
    val id: Id,
    val title: String,
    val description: String?,
    val cover: String?,
    val publishDate: Instant = Clock.System.now(),
    val updateDate: Instant = publishDate,
)
