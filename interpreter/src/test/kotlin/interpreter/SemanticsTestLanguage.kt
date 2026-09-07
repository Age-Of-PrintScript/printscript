package interpreter

import domain.Failure
import domain.NumType
import domain.PSLiteral
import domain.PSOperator
import domain.StrType
import domain.Success

val printlnFunction =
    BuiltInFunction { args ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        Success(
            FunctionResult(
                returnValue = null,
                events = listOf(PrintEvent(message)),
            ),
        )
    }

val testBuiltInFunctions: Map<String, BuiltInFunction> =
    mapOf(
        "println" to printlnFunction,
    )

enum class Operators(
    override val symbol: String,
    override val precedence: Int,
) : PSOperator {
    SUM("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
}

val sumNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() + r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val sumStrAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val sumStrAndNum =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val sumNumAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val subNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() - r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val multNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() * r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val divNumAndNum =
    BinaryOperation { l, r ->
        val rightVal = r.raw.toDouble()
        if (rightVal == 0.0) {
            Failure(RuntimeError.MATH_ERROR)
        } else {
            val result = l.raw.toDouble() / rightVal
            Success(PSLiteral(result.toString(), NumType))
        }
    }

val multNumAndString =
    BinaryOperation { l, r ->
        val times =
            l.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)
        Success(PSLiteral(r.raw.repeat(times.toInt()), StrType))
    }

val multStringAndNum =
    BinaryOperation { l, r ->
        val times =
            r.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)

        Success(PSLiteral(l.raw.repeat(times.toInt()), StrType))
    }

val testBinaryOperations: Map<OperationKey, BinaryOperation> =
    mapOf(
        OperationKey(Operators.SUM, NumType, NumType) to sumNumAndNum,
        OperationKey(Operators.SUM, StrType, StrType) to sumStrAndStr,
        OperationKey(Operators.SUM, StrType, NumType) to sumStrAndNum,
        OperationKey(Operators.SUM, NumType, StrType) to sumNumAndStr,
        OperationKey(Operators.SUBTRACT, NumType, NumType) to subNumAndNum,
        OperationKey(Operators.MULTIPLY, NumType, NumType) to multNumAndNum,
        OperationKey(Operators.MULTIPLY, NumType, StrType) to multNumAndString,
        OperationKey(Operators.MULTIPLY, StrType, NumType) to multStringAndNum,
        OperationKey(Operators.DIVIDE, NumType, NumType) to divNumAndNum,
    )

val testSemantics: LanguageSemantics = LanguageSemantics(testBuiltInFunctions, testBinaryOperations)
