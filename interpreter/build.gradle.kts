plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":domain"))
}