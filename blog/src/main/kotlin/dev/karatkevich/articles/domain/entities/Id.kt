package dev.karatkevich.articles.domain.entities

@JvmInline
value class Id(val value: String) {
    companion object {
        val EMPTY = Id("")
    }
}

fun Id.isEmpty() = value.isEmpty()

fun String.toId() = Id(this)
