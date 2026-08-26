plugins {
    id("printscript.common-conventions")
}

dependencies {
    implementation(project(":interpreter"))
    implementation(project(":lexer"))
    implementation(project(":parser"))
}
