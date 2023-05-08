package dev.karatkevich.articles.domain.entities

data class Article(
    val id: Id,
    val title: String,
    val description: String?,
    val cover: String?,
)
