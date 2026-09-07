package engine.ps_versions.v1_1

import engine.ps_versions.v1_0.v1_0Keywords
import tokens.Call
import tokens.Const
import tokens.DataType
import tokens.Else
import tokens.If
import tokens.TokenType

val v1_1Keywords: Map<String, TokenType> =
    v1_0Keywords +
        mapOf(
            "const" to Const,
            "if" to If,
            "else" to Else,
            "readInput" to Call("readInput"),
            "readEnv" to Call("readEnv"),
            "boolean" to DataType(BoolType),
        )
