plugins {
    id("printscript.common-conventions")
}

dependencies {
    api(project(":tokens"))
    api(project(":ast"))
    testImplementation(kotlin("test"))
}
