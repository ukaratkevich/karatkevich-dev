plugins {
    `java-library`
    kotlin

    id("kotlin-convention")
}

kotlin {
    jvmToolchain(11)
}

tasks.test {
    useJUnitPlatform()
}
