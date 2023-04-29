package dev.karatkevich.articles.validation

import dev.karatkevich.articles.models.Article
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.routing.Route

internal fun Route.articleValidation() {
    install(RequestValidation) {
        validate<Article.New> { article ->
            ValidationResult.Valid
        }
    }
}
