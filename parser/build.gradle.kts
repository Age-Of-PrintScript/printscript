plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
dependencies {
    api(project(":tokens"))
    api(project(":ast"))
    testImplementation(kotlin("test"))
}