import io.gitlab.arturbosch.detekt.Detekt

plugins {
    base
    id("detekt-base-convention")
}

val detektProject = tasks.register<Detekt>("detektProject") {
    description = "Runs detekt on the project without starting overhead for modules"

    setSource(files(projectDir))
}

tasks.named("check").configure {
    dependsOn(detektProject)

    // disabling root detekt task
    setDependsOn(
        dependsOn.filterNot { it is TaskProvider<*> && it.name == "detekt" }
    )
}
