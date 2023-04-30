package dev.karatkevich.articles.models

import dev.karatkevich.articles.models.Article.Existing
import dev.karatkevich.articles.models.Article.New
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class Article {

    @Serializable
    data class New(
        @SerialName("title") val title: String,
        @SerialName("description") val description: String? = null,
        @SerialName("cover") val cover: String? = null,
    ) : Article()

    @Serializable
    data class Existing(
        @SerialName("id") val id: String,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String? = null,
        @SerialName("cover") val cover: String? = null,
    ) : Article()
}

internal fun New.toExisting(id: String): Existing {
    return Existing(
        id = id,
        title = title,
        cover = cover,
        description = description,
    )
}
