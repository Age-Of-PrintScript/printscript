# Como se crean modulos

---

1. Crear la nueva carpeta en la raiz del repo
2. Agregar un archivo build.gradle.kts
3. Ponerle esto:
```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}
```
4. Agregar una carpeta src, adentro una carpeta main y adentro una kotlin (dentro de esta ultima va todo el codigo)
5. En el archivo global settings.gradle.kts agregar acá
```kotlin
include("domain", "lexer", "parser", "interpreter", "tokens", "ast")
```
El nuevo modulo:
```kotlin
include("nombre_del_nuevo", "domain", "lexer", "parser", "interpreter", "tokens", "ast")
```
6. Rebuildear gradle desde el elefantito
## opcional (dependencias)

Si queremos que nuestro modulo dependa e importe cosas de otro
agregar en el build.gradle.kts
```kotlin
dependencies {
    implementation(project(":nombre_del_modulo_a_depender"))
}
```

