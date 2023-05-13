import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "build-logic"

tasks.withType<KotlinCompile>().configureEach {
    val kotlinLanguageVersion = libs.versions.kotlinLanguageVersionBuildScript.get()
    val jvmTargetVersion = libs.versions.jvmTargetBuildScript.get()

    kotlinOptions {
        jvmTarget = jvmTargetVersion

        languageVersion = kotlinLanguageVersion
        apiVersion = kotlinLanguageVersion
    }
}

dependencies {
    implementation(libs.kotlinPlugin)
    implementation(libs.detektPlugin)

    // Enables the use of version catalogue inside precompiled plugins
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
