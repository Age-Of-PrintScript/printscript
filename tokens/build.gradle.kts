plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}
dependencies {
    api(project(":domain"))

}