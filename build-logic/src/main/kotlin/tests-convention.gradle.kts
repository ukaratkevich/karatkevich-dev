import dev.karatkevich.configureIntegrationTests

plugins {
    kotlin("jvm")
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
                    implementation(libs.mockk)
                }
            }
        }
    }
}

// By default, this configuration extends only `api`, but not `implementation`,
// without this line we need to redeclare `main` dependencies
// https://github.com/gradle/gradle/issues/19497
configurations["integrationTestImplementation"]
    .extendsFrom(configurations["implementation"])

tasks.check {
    dependsOn(testing.suites.getByName("integrationTest"))
}
