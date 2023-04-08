import io.gitlab.arturbosch.detekt.Detekt

plugins {
    base
    id("detekt-base-convention")
}

val detektProject = tasks.register<Detekt>("detektProject") {
    description = "Runs detekt on the project without starting overhead for modules"

    setSource(files(projectDir))

    this.config.setFrom(files(project.rootDir.resolve("config/detekt/detekt.yml")))
    buildUponDefaultConfig = false
}

tasks.named("check").configure {
    dependsOn(detektProject)

    // disabling root detekt task
    setDependsOn(
        dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" }
    )
}
