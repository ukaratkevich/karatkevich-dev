package dev.karatkevich

import io.ktor.server.application.Application

fun Application.module() {
    configurePlugins()

    blogModule()
}
