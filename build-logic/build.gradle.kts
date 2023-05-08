import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "build-logic"

tasks.withType<KotlinCompile>().configureEach {
    // maybe at some point build scripts can live with the separate kotlin version?
    val kotlinLanguageVersion = libs.versions.kotlinLanguageVersionsGradle.get()

    kotlinOptions {
        jvmTarget = "17"

        languageVersion = kotlinLanguageVersion
        apiVersion = kotlinLanguageVersion
    }
}

dependencies {
    implementation(libs.kotlinPlugin)
    implementation(libs.detektPlugin)
//    implementation(libs.kotlinSerialization)

    // Enables the use of version catalogue inside precompiled plugins
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
