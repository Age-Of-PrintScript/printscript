package interpreter

import domain.Either
import domain.PSLiteral

fun interface Function {
    fun execute(
        args: List<PSLiteral>,
        runtimeEnvironment: RuntimeEnvironment,
    ): Either<RuntimeError, FunctionResult>
}

data class FunctionResult(
    val returnValue: PSLiteral? = null,
    val events: List<RuntimeEvents> = emptyList(),
)
