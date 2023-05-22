package dev.karatkevich

import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.testing.base.TestingExtension

internal fun TestingExtension.configureIntegrationTests(configure: JvmTestSuite.() -> Unit) {
    val integrationTest = suites.findByName("integrationTest") as? JvmTestSuite

    if (integrationTest == null) {
        suites.create("integrationTest", JvmTestSuite::class.java) {
            configure()
        }
    } else {
        integrationTest.configure()
    }
}
