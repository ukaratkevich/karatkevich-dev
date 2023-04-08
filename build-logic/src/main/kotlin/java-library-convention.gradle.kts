plugins {
    `java-library`
    kotlin

    id("kotlin-convention")
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchainVersion.get().toInt())
}

tasks.test {
    useJUnitPlatform()
}
