@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library-convention")
    alias(libs.plugins.kotlinSerialization)
}

group = "dev.karatkevich"

dependencies {
    implementation(libs.bundles.ktorServer)

    testImplementation(libs.bundles.tests)
}
