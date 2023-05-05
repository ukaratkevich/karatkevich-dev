import gradle.kotlin.dsl.accessors._b8e9b34270198d14a4ec0d8967890a50.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchainVersion.get().toInt())
}

tasks.withType<KotlinCompile>().configureEach {
    val kotlinVersion = libs.versions.kotlinLanguageVersionsGradle.get()

    kotlinOptions {
        jvmTarget = "11"

        languageVersion = kotlinVersion
        apiVersion = kotlinVersion

        allWarningsAsErrors = true

        freeCompilerArgs += listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}
