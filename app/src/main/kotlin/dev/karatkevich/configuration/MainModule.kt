package dev.karatkevich.configuration

import dev.karatkevich.blog.blogModule
import io.ktor.server.application.Application

fun Application.module() {
    configurePlugins()

    blogModule()
}
