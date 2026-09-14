package interpreter

import domain.Either
import domain.PSLiteral
import domain.PSType

data class CastKey(
    val from: PSType,
    val to: PSType,
)

fun interface TypeCast {
    fun cast(value: PSLiteral): Either<RuntimeError, PSLiteral>
}
