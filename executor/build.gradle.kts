plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":interpreter"))
    implementation(project(":lexer"))
    implementation(project(":parser"))
}
