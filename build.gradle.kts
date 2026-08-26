import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    // Solo aplicamos estas herramientas a módulos que ya tienen el plugin de Kotlin
    // (evita romper si en algún momento agregan un módulo no-Kotlin).
    plugins.withId("org.jetbrains.kotlin.jvm") {
        apply(plugin = "org.jlleitschuh.gradle.ktlint")
        apply(plugin = "io.gitlab.arturbosch.detekt")
        apply(plugin = "jacoco")
        dependencies {
            add("detektPlugins", "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
        }

        extensions.configure<DetektExtension> {
            buildUponDefaultConfig = true
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
            autoCorrect = true
        }

        tasks.withType<Test> {
            useJUnitPlatform()
            finalizedBy(tasks.named("jacocoTestReport"))
        }

        tasks.named<JacocoReport>("jacocoTestReport") {
            dependsOn(tasks.named("test"))
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }

        tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
            dependsOn(tasks.named("jacocoTestReport"))
            violationRules {
                rule {
                    limit {
                        minimum = "0.80".toBigDecimal()
                    }
                }
            }
        }

        tasks.named("check") {
            dependsOn(tasks.named("jacocoTestCoverageVerification"))
        }
        tasks.register<Copy>("installGitHooks") {
            description = "Copies git hooks from /hooks to /.git/hooks with execution permissions"
            group = "git hooks"
            from("$rootDir/hooks")
            into("$rootDir/.git/hooks")
            filePermissions {
                user {
                    read = true
                    write = true
                    execute = true
                }
                group {
                    read = true
                    execute = true
                }
                other {
                    read = true
                    execute = true
                }
            }
        }
    }
}
