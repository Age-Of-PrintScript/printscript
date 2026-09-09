plugins {
   id("printscript.common-conventions")
}


dependencies {
    implementation(project(":lexer"))
    implementation(project(":parser"))
    implementation(project(":versionFactory"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

