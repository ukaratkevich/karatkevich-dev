plugins {
    kotlin("jvm") version "1.8.0"
}

group = "dev.karatkevich"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}