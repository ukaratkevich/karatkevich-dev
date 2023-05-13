import dev.karatkevich.configureIntegrationTests

plugins {
    java
    `jvm-test-suite`
}

testing {
    suites {
        val test by getting(JvmTestSuite::class)

        configureIntegrationTests {
            testType.set(TestSuiteType.INTEGRATION_TEST)

            dependencies {
                implementation(project)
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }

        configureEach {
            if (this is JvmTestSuite) {
                useJUnitJupiter()

                dependencies {
                    implementation(libs.kotestRunner)
                    implementation(libs.kotestAssertions)
                }
            }
        }
    }
}

tasks.check {
    dependsOn(testing.suites.getByName("integrationTest"))
}
