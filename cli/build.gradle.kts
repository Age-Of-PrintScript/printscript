plugins {
    id("printscript.common-conventions")
    application
}

dependencies {
    implementation(project(":engine"))
    implementation(project(":linter"))
    implementation(project(":formatter"))
    implementation(libs.clikt)
}

application {
    mainClass.set("cli.MainKt")
}
tasks.named<JavaExec>("run") {
    workingDir = rootDir
}
