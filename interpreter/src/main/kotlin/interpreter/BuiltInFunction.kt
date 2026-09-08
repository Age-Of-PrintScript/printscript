package interpreter

import domain.Either
import domain.PSLiteral
import interpreter.environment.Event

fun interface BuiltInFunction {
    fun execute(args: List<PSLiteral>): Either<RuntimeError, FunctionResult>
}

data class FunctionResult(
    val returnValue: PSLiteral? = null,
    val events: List<Event> = emptyList(),
)
