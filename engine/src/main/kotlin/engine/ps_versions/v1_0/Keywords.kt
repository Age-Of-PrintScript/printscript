package engine.ps_versions.v1_0

import domain.NumType
import domain.StrType
import tokens.Call
import tokens.DataType
import tokens.Let
import tokens.TokenType

val v1_0Keywords: Map<String, TokenType> =
    mapOf(
        "let" to Let,
        "println" to Call("println"),
        "number" to DataType(NumType),
        "string" to DataType(StrType),
    )
