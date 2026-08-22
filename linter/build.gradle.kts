plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "2.2.0"
}

repositories {
    mavenCentral()
}

dependencies{
    implementation(project(":interpreter"))
    implementation(project(":lexer"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(project(":parser"))
    testImplementation(kotlin("test"))
}