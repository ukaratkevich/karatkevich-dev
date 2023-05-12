plugins {
    id("tests-base-convention")
    kotlin("jvm")
}

sourceSets {
    val main by getting
    val test by getting

    create("integrationTest") {
        java.setSrcDirs(files("src/integrationTest/kotlin"))
        resources.setSrcDirs(files("src/integrationTest/resources"))

        compileClasspath += main.output + test.output
        runtimeClasspath += main.output + test.output
    }
}

configurations {
    val integrationTest by sourceSets.getting

    get(integrationTest.compileOnlyConfigurationName)
        .extendsFrom(configurations.testCompileOnly.get())

    get(integrationTest.implementationConfigurationName)
        .extendsFrom(configurations.testImplementation.get())

    get(integrationTest.runtimeOnlyConfigurationName)
        .extendsFrom(configurations.testRuntimeOnly.get())
}

val integrationTestTask = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = JavaBasePlugin.VERIFICATION_GROUP

    val integrationTest by sourceSets.getting

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    shouldRunAfter(tasks.named("test"))
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
