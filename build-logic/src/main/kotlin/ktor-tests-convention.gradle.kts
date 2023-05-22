import dev.karatkevich.configureIntegrationTests

plugins {
    id("tests-convention")
}

testing {
    configureIntegrationTests {
        dependencies {
            implementation(libs.ktorTestHost)
            implementation(libs.kotestKtor)
            implementation(libs.ktorClientContentNegotiation)
        }
    }
}
