package dev.karatkevich.blog.articles.domain.entities

@JvmInline
value class Id private constructor(val value: String) {
    companion object {
        val EMPTY = Id("")

        fun String.toId() = Id(this)
    }
}

fun Id.isEmpty() = value.isEmpty()
