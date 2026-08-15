plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":tokens"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}