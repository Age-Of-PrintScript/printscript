package versionfactory.v1_0

import domain.Failure
import domain.NumType
import domain.PSLiteral
import domain.StrType
import domain.Success
import interpreter.BinaryOperation
import interpreter.RuntimeError

internal val sumNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() + r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

internal val sumStrAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

internal val sumStrAndNum =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

internal val sumNumAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

internal val subNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() - r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

internal val multNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() * r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

internal val divNumAndNum =
    BinaryOperation { l, r ->
        val rightVal = r.raw.toDouble()
        if (rightVal == 0.0) {
            Failure(RuntimeError.MATH_ERROR)
        } else {
            val result = l.raw.toDouble() / rightVal
            Success(PSLiteral(result.toString(), NumType))
        }
    }

internal val multNumAndString =
    BinaryOperation { l, r ->
        val times =
            l.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)
        Success(PSLiteral(r.raw.repeat(times.toInt()), StrType))
    }

internal val multStringAndNum =
    BinaryOperation { l, r ->
        val times =
            r.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)

        Success(PSLiteral(l.raw.repeat(times.toInt()), StrType))
    }
