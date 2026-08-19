plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies{
    implementation(project(":interpreter"))
    implementation(project(":lexer"))
    implementation(project(":parser"))
}