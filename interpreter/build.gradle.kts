plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
dependencies {
    api(project(":ast"))

}