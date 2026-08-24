---
name: pr-reviewer
description: Revisa Pull Requests de código Kotlin evaluando correctness, diseño (SOLID/acoplamiento), legibilidad, consistencia con el codebase y alcance del PR. Usar cuando se pida revisar un PR, revisar cambios, hacer code review, o evaluar si un diff está listo para mergear.
---

# PR Reviewer (Kotlin)

Este skill guía la revisión de Pull Requests en un proyecto Kotlin. El objetivo es dar
feedback accionable, priorizado por severidad, sin ahogar al autor con ruido de bajo valor.

## Contexto del proyecto

- Lenguaje: **Kotlin**.
- Antes de revisar, si no lo tenés ya en contexto, mirá el codebase para entender:
  - Convenciones de naming y estructura de paquetes ya existentes.
  - Si el proyecto usa coroutines, Flow, RxKotlin, o es sync.
  - Si hay linter/formatter configurado (ktlint, detekt) — si existe, confiá en que
    ese tooling ya cubre estilo mecánico (indentación, imports, etc.) y **no lo repitas
    en la review**; enfocate en lo que un linter no puede detectar.

## Cómo reportar

Agrupá los hallazgos por categoría (ver abajo) y asignale a cada uno una severidad:

- 🔴 **Bloqueante** — no debería mergearse así. Bugs, rompe el build, viola un principio
  de diseño de forma grave, o mezcla scope de forma que hace la PR imposible de revisar bien.
- 🟡 **Sugerencia** — vale la pena cambiarlo pero no bloquea el merge. Mejora real de
  diseño/legibilidad/consistencia.
- ⚪ **Nitpick** — cosmético u opinión menor. Aclarar que es opcional.

Si una categoría no tiene hallazgos, no la menciones (no rellenar con "todo bien acá").
No repitas el mismo hallazgo en variantes distintas.

## Checklist de revisión

### 1. Correctness (🔴 por default si falla)
- ¿El código compila? ¿Hay algo que rompería en tiempo de compilación o de forma obvia
  en runtime (NPE evidente, `!!` injustificado, unwrap de nullable sin chequeo)?
- ¿La lógica hace lo que el PR dice que hace? Leé la descripción/título del PR y
  contrastalo con el diff.
- ¿Hay bugs críticos: off-by-one, condiciones de carrera obvias en coroutines
  (shared mutable state sin sincronización), manejo incorrecto de nulls,
  excepciones no capturadas que deberían estarlo?
- ¿Los edge cases evidentes están contemplados (listas vacías, nulls, valores límite)?

### 2. Diseño (SOLID / acoplamiento)
- **SRP**: ¿la clase/función hace una sola cosa? ¿Hay una clase "God object" haciendo de todo?
- **Acoplamiento**: ¿depende de detalles concretos donde podría depender de una
  interfaz/abstracción? ¿Hay dependencias circulares o un módulo que sabe demasiado
  de la implementación interna de otro?
- **OCP/LSP/ISP/DIP**: aplicalos con criterio — no todo Kotlin idiomático necesita
  una interfaz para todo. Marcá esto como sugerencia salvo que el acoplamiento
  genere un problema real (difícil de testear, difícil de extender, rompe otras clases).
- ¿Se usa inyección de dependencias donde correspondería en vez de instanciar
  directamente (`ClassA()`) dentro de otra clase?
- Uso idiomático de Kotlin: data classes para modelos, sealed classes/interfaces
  para jerarquías cerradas, extension functions con criterio (no abusar).

### 3. Legibilidad
- ¿El código es claro sin necesitar comentarios que expliquen "qué hace"?
- ¿Es innecesariamente verboso? (helpers que podrían ser una expresión con scope
  functions de Kotlin: `let`, `apply`, `also`, `run` — pero sin abusar y volverlo
  ilegible al revés).
- Nombres de variables/funciones: ¿comunican intención? Evitar nombres genéricos
  (`data`, `result`, `temp`, `handleX` sin especificar qué maneja).
- Funciones largas que deberían dividirse en pasos con nombre.
- Anidamiento profundo de `if`/`when` que se podría aplanar con early return.

### 4. Consistencia con el codebase
- ¿Sigue las convenciones de naming/estructura que ya existen en el proyecto?
- ¿Reinventa algo que ya existe como util, extension function o helper en otro lado
  del código? (buscar duplicación antes de aprobar).
- Si rompe un patrón establecido (ej: todos los repositorios devuelven `Result<T>`
  y este devuelve nullable), preguntar si es intencional — marcarlo como 🟡 salvo
  que genere inconsistencia real de contrato (entonces 🔴).

### 5. Alcance del PR
- ¿La PR hace una sola cosa, o mezcla features/fixes/refactors no relacionados?
- ¿El tamaño es razonable para revisar con atención, o debería partirse?
- Si mezcla scope: marcarlo como 🔴 o 🟡 según qué tan mezclado esté — si hace
  imposible revisar bien el cambio principal, es bloqueante; si es un cambio
  chico "de paso" (ej: rename menor), puede ser solo una nota.

## Qué NO hacer

- No comentar sobre performance salvo que sea algo obsceno (ej: un loop anidado
  claramente cuadrático sobre una colección que se sabe grande, o una query/IO
  dentro de un loop que debería estar afuera). Si dudás si es "obsceno" o no, no lo reportes.
- No comentar sobre documentación faltante ni sobre concurrencia salvo que la
  concurrencia sea, de hecho, un bug de correctness (en ese caso cae en la
  categoría 1, no es un ítem aparte).
- No repetir lo que ya cubre el linter/formatter automático del proyecto.
- No dar 10 nitpicks por cada bloqueante — priorizá señal sobre ruido.

## Formato de salida sugerido

```
## Review de PR: <título>

### 🔴 Bloqueante
- [Categoría] Descripción del problema + por qué + sugerencia concreta.

### 🟡 Sugerencia
- [Categoría] ...

### ⚪ Nitpick
- [Categoría] ...

### Resumen
Aprobar / Aprobar con cambios menores / Cambios requeridos — en una línea, con la razón principal.
```
