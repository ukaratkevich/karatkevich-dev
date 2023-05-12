plugins {
    id("integration-tests-convention")
}

dependencies {
    integrationTestImplementation(libs.ktorTestHost)
}

fun <T> DependencyHandlerScope.integrationTestImplementation(dependency: Provider<T>) {
    "integrationTestImplementation"(dependency)
}
