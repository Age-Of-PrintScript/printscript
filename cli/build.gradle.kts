plugins {
    id("printscript.common-conventions")
    application
}

dependencies {
    implementation(project(":engine"))
    implementation(libs.clikt)
}

application {
    mainClass.set("cli.MainKt")
}
