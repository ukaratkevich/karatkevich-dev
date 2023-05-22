@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("ktor-library-convention")
    alias(libs.plugins.kotlinSerialization)
}

dependencies {
    implementation(libs.kotlinDateTime)
}
