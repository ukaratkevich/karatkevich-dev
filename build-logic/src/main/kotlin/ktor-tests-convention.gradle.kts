plugins {
    id("tests-base-convention")
    kotlin("jvm")
}

sourceSets {
    val main by getting

    create("integrationTest") {
        java.setSrcDirs(files("src/integrationTest/kotlin"))
        resources.setSrcDirs(files("src/integrationTest/resources"))

        compileClasspath += main.output
        runtimeClasspath += main.output
    }
}

configurations {
    val integrationTest by sourceSets.getting

    get(integrationTest.implementationConfigurationName)
        .extendsFrom(configurations.testImplementation.get())

    get(integrationTest.runtimeOnlyConfigurationName)
        .extendsFrom(configurations.testRuntimeOnly.get())
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"

    val integrationTest by sourceSets.getting

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
}

tasks.named("check") {
    dependsOn(integrationTestTask)
}

dependencies {
    integrationTestImplementation(libs.ktorTestHost)
}

fun <T> DependencyHandlerScope.integrationTestImplementation(dependency: Provider<T>) {
    "integrationTestImplementation"(dependency)
}
