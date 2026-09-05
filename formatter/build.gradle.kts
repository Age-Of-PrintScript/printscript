plugins {
   id("printscript.common-conventions")
}


dependencies {
    implementation(project(":ast"))
    implementation(project(":domain"))
    implementation(project(":lexer"))
    implementation(project(":parser"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

