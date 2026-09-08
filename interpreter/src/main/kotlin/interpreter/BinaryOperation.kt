package interpreter

import domain.Either
import domain.PSLiteral
import domain.PSOperator
import domain.PSType

fun interface BinaryOperation {
    fun apply(
        left: PSLiteral,
        right: PSLiteral,
    ): Either<RuntimeError, PSLiteral>
}

data class OperationKey(
    val operator: PSOperator,
    val leftType: PSType,
    val rightType: PSType,
)
