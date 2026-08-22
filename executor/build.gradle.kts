plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.jvm)
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

dependencies{
    implementation(project(":interpreter"))
    implementation(project(":lexer"))
    implementation(project(":parser"))
}