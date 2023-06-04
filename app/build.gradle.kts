@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("ktor-application-convention")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

version = "1.0"

sqldelight {
    databases {
        create("Database") {
            packageName.set("dev.karatkevich")

            verifyMigrations.set(true)
            deriveSchemaFromMigrations.set(true)
        }
    }
}

dependencies {
    implementation(libs.kotlinDateTime)
    implementation(libs.sqldelight.sqlite.driver)
}
