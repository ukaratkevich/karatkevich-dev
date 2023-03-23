plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlinPlugin)

    // Enables the use of version catalogue inside precompiled plugins
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}