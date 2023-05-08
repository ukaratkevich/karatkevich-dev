plugins {
    id("java-library-convention")
    id("ktor-tests-convention")
}

group = "dev.karatkevich"

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.logger)
}
