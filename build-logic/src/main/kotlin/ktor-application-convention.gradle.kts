plugins {
    application
    id("kotlin-convention")
}

group = "karatkevichdev"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
