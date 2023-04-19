plugins {
    `java-library`
    id("kotlin-convention")
}

tasks.test {
    useJUnitPlatform()
}
