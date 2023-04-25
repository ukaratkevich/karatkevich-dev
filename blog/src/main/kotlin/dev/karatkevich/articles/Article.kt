package dev.karatkevich.articles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
)
