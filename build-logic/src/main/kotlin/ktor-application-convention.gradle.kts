plugins {
    application
    id("kotlin-convention")
    id("ktor-tests-convention")
}

group = "karatkevichdev"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
