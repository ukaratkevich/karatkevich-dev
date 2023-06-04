@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("detekt-project-convention")
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.sqldelight) apply false
}

group = "karatkevichdev"
