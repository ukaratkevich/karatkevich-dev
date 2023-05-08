@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("java-library-convention")
    alias(libs.plugins.kotlinSerialization)
}

group = "dev.karatkevich"

dependencies {
    implementation(libs.ktorCore)
    implementation(libs.ktorResources)
    implementation(libs.ktorValidation)
    implementation(libs.logger)
    implementation(libs.ktorSerializationJson)
    implementation(libs.kotlinCoroutines)

    testImplementation(libs.bundles.tests)
}
