import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchainVersion.get().toInt())
}

tasks.withType<KotlinCompile>().configureEach {
    val kotlinVersion = libs.versions.kotlinLanguageVersionGradle.get()

    kotlinOptions {
        jvmTarget = "11"

        languageVersion = kotlinVersion
        apiVersion = kotlinVersion

        allWarningsAsErrors = true

        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}

dependencies {
    implementation(libs.kotlinCoroutines)
}
