import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile>().configureEach {
    val kotlinVersion = libs.versions.kotlinLanguageVersionsGradle.get()

    kotlinOptions {
        jvmTarget = "11"

        languageVersion = kotlinVersion
        apiVersion = kotlinVersion

        allWarningsAsErrors = true
    }
}
