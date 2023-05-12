plugins {
    id("tests-base-convention")
    idea
}

val integrationTest: SourceSet by sourceSets.creating {
    val main by sourceSets.getting
    val test by sourceSets.getting

    compileClasspath += main.output + test.output
    runtimeClasspath += main.output + test.output
}

// https://youtrack.jetbrains.com/issue/IDEA-218815/
// Fore some reason with Gradle 7.5+ it's relevant here
idea {
    module {
        testSourceDirs.addAll(integrationTest.allSource.srcDirs)
    }
}

configurations {
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

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    shouldRunAfter(tasks.named("test"))
}

tasks.named("check") {
    dependsOn(integrationTestTask)
}
