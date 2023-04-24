plugins {
    application
    id("kotlin-convention")
}

group = "dev.karatkevich"
version = "1.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
