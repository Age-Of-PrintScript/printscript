package interpreter

import domain.Either
import domain.PSLiteral

fun interface BuiltInFunction {
    fun execute(
        args: List<PSLiteral>,
        io: InterpreterIO,
    ): Either<RuntimeError, PSLiteral?>
}
