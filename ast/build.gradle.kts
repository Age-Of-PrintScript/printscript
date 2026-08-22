plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`

    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}
dependencies {
    api(project(":domain"))
}