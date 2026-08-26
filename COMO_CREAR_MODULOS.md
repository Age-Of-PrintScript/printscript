# Cómo se crean módulos

---

1. Crear la nueva carpeta en la raíz del repo (ej: `formatter`).
2. Agregar un archivo `build.gradle.kts` dentro de la carpeta del nuevo módulo.
3. Ponerle únicamente esto:
```kotlin
plugins {
    id("printscript.common-conventions")
}
```
*(Esto aplica automáticamente Kotlin JVM, Detekt, KtLint, JaCoCo, JUnit 5 y configuración de calidad).*

4. Agregar la estructura de carpetas: `src/main/kotlin` (dentro de esta va todo el código).
5. En el archivo `settings.gradle.kts` agregar el nuevo módulo en `include(...)`:
```kotlin
include("domain", "lexer", "parser", "interpreter", "tokens", "ast", "executor", "nombre_del_nuevo")
```
6. Rebuildear Gradle / hacer click en el elefante de Gradle en IntelliJ IDEA.

## Dependencias entre módulos (opcional)

Si queremos que nuestro módulo dependa de otro módulo del proyecto (por ejemplo `tokens` o `ast`):
```kotlin
plugins {
    id("printscript.common-conventions")
}

dependencies {
    api(project(":tokens"))
    // o implementation(project(":ast"))
}
```
