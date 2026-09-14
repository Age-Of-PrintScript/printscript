package versionfactory.v1_1

import domain.BoolType
import domain.Failure
import domain.NumType
import domain.PSLiteral
import domain.StrType
import domain.Success
import interpreter.CastKey
import interpreter.RuntimeError
import interpreter.TypeCast

internal val strToNum =
    TypeCast { value ->
        val num =
            value.raw.toDoubleOrNull()
                ?: return@TypeCast Failure(RuntimeError.INVALID_CAST)
        Success(PSLiteral(num.toString(), NumType))
    }

internal val strToBool =
    TypeCast { value ->
        val bool =
            value.raw.toBooleanStrictOrNull()
                ?: return@TypeCast Failure(RuntimeError.INVALID_CAST)
        Success(PSLiteral(bool.toString(), BoolType))
    }

internal val v1_1TypeCasters: Map<CastKey, TypeCast> =
    mapOf(
        CastKey(StrType, NumType) to strToNum,
        CastKey(StrType, BoolType) to strToBool,
    )
