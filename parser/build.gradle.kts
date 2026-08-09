plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":tokens"))
    implementation(project(":ast"))
    testImplementation(kotlin("test"))
}