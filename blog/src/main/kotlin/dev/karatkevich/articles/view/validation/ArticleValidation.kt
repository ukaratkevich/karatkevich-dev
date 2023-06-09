package dev.karatkevich.articles.view.validation

import dev.karatkevich.articles.view.ArticleRepresentation
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.routing.Route

private const val MAX_TITLE_LENGTH = 50

fun Route.installArticlesValidation() {
    install(RequestValidation) {
        validate<ArticleRepresentation.Request> { article ->
            val messages = mutableListOf<String>()

            if (article.title.length > MAX_TITLE_LENGTH) {
                messages += """Article has exceeded the maximum length of $MAX_TITLE_LENGTH,
                    |current length is ${article.title.length}
                """.trimMargin()
            }

            if (messages.isEmpty()) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(messages)
            }
        }
    }
}
