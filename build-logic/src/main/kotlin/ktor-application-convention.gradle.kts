plugins {
    application
    id("kotlin-convention")
}

group = "dev.karatkevich"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
