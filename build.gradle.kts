@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("detekt-project-convention")
    alias(libs.plugins.kotlinJvm) apply false
}

group = "dev.karatkevich"
