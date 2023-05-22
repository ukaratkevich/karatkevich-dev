plugins {
    id("java-library-convention")
    id("ktor-tests-convention")
}

group = "karatkevichdev"

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
