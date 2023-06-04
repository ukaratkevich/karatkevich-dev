package dev.karatkevich.blog.articles.view

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ArticleRepresentation {
    @Serializable
    data class Request(
        @SerialName("title") val title: String,
        @SerialName("description") val description: String? = null,
        @SerialName("cover") val cover: String? = null,
    ) : ArticleRepresentation()

    @Serializable
    data class Response(
        @SerialName("uid") val uid: String,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String? = null,
        @SerialName("cover") val cover: String? = null,
        @SerialName("published") val published: String,
        @SerialName("updated") val updated: String,
    )
}
