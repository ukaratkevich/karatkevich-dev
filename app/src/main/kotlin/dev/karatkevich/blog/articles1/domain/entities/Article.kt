package dev.karatkevich.blog.articles1.domain.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Article(
    val uid: Id,
    val title: String,
    val description: String?,
    val cover: String?,
    val publishDate: Instant = Clock.System.now(),
    val updateDate: Instant = publishDate,
)
