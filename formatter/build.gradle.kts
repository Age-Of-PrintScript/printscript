

plugins {
    alias(libs.plugins.kotlin.jvm)

}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":ast"))
    api(project(":domain"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(project(":lexer"))
    implementation(project(":parser"))

}

