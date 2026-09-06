package executor.ps_versions.v1_0

import domain.Failure
import domain.NumType
import domain.PSLiteral
import domain.StrType
import domain.Success
import interpreter.BinaryOperation
import interpreter.RuntimeError

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
                ?.takeIf { it >= 0 && it % 1.0 == 0.0 }
                ?.toInt()
                ?: return@BinaryOperation Failure(RuntimeError.STRING_CONCATENATION_NEEDS_INT)
        Success(PSLiteral(r.raw.repeat(times), StrType))
    }

val multStringAndNum =
    BinaryOperation { l, r ->
        val times =
            r.raw
                .toDoubleOrNull()
                ?.takeIf { it >= 0 && it % 1.0 == 0.0 }
                ?.toInt()
                ?: return@BinaryOperation Failure(RuntimeError.STRING_CONCATENATION_NEEDS_INT)
        Success(PSLiteral(l.raw.repeat(times), StrType))
    }
