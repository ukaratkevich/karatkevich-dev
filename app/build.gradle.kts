@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("ktor-application-convention")
    alias(libs.plugins.kotlinSerialization)
}

version = "1.0"

dependencies {
    implementation(projects.blog)
}
