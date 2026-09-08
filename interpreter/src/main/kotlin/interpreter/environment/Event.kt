package interpreter.environment

sealed interface Event

data class PrintEvent(
    val message: String,
) : Event
