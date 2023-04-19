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
}

tasks.named<JavaExec>("run") {
    doFirst {
        environment("DEBUG", project.properties["debug"] as String)
        environment("HOST", project.properties["host"] as String)
        environment("PORT", project.properties["port"] as String)
    }
}
