import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(libs.detektFormatting)
}

detekt {
    toolVersion = libs.versions.detektVersion.get()

    basePath = rootProject.projectDir.absolutePath

    source = files(
        "src/main/kotlin",
        "src/main/java",
    )

    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = false

    parallel = true

    ignoreFailures = false
}

tasks.withType(Detekt::class).configureEach {
    reports {
        sarif.required.set(true)
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
        md.required.set(false)
    }

    exclude("**/build/**")
    exclude("**/resources/**")
}
