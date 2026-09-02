plugins {
    id("printscript.common-conventions")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    implementation(project(":lexer"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(project(":parser"))
    testImplementation(kotlin("test"))
}
