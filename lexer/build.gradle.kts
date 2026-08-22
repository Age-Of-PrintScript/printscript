plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.jvm)
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}
dependencies {
    api(project(":tokens"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}