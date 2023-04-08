import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    detektPlugins(libs.detektFormatting)
}

detekt {
    toolVersion = libs.versions.detektVersion.get()

    buildUponDefaultConfig = false
}

tasks.withType(Detekt::class.java).configureEach {
    reports {
        sarif.required.set(true)
        xml.required.set(false)
        html.required.set(false)
        txt.required.set(false)
        md.required.set(false)
    }
}


