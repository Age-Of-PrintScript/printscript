package interpreter.cases

import interpreter.PrintEvent
import interpreter.RuntimeEvents

val EMPTY_EVENTS = RuntimeEvents(emptyList())

val EVENTS_WITH_PRINT_HOLA =
    RuntimeEvents(
        listOf(PrintEvent("hola")),
    )

val EVENTS_WITH_PRINT_5 =
    RuntimeEvents(
        listOf(PrintEvent("5")),
    )

val EVENTS_WITH_PRINT_10 =
    RuntimeEvents(
        listOf(PrintEvent("10")),
    )

val EVENTS_WITH_PRINT_3 =
    RuntimeEvents(
        listOf(PrintEvent("3.0")),
    )

val EVENTS_WITH_PRINT_5_THEN_10 =
    RuntimeEvents(
        listOf(PrintEvent("5"), PrintEvent("10")),
    )

val EVENTS_WITH_PRINT_HOLA_THEN_MUNDO =
    RuntimeEvents(
        listOf(PrintEvent("hola"), PrintEvent(" mundo")),
    )

val EVENTS_WITH_PRINT_HOLA_5 =
    RuntimeEvents(
        listOf(PrintEvent("hola5")),
    )

val EVENTS_WITH_PRINT_5_HOLA =
    RuntimeEvents(
        listOf(PrintEvent("5hola")),
    )

val EVENTS_WITH_PRINT_HOLA_MUNDO =
    RuntimeEvents(
        listOf(PrintEvent("hola mundo")),
    )
