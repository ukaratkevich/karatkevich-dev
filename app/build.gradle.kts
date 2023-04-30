@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    id("kotlin-convention")
    alias(libs.plugins.kotlinSerialization)
}

group = "dev.karatkevich"
version = "1.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.ktorResources)
    implementation(libs.ktorContentNegotiation)
    implementation(libs.ktorSerializationJson)

    implementation(libs.logger)

    implementation(projects.blog)
}
