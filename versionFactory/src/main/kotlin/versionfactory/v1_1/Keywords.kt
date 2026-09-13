package versionfactory.v1_1

import domain.BoolType
import tokens.Call
import tokens.Const
import tokens.DataType
import tokens.Else
import tokens.If
import tokens.Literal
import tokens.TokenType
import versionfactory.v1_0.v1_0Keywords

internal val v1_1Keywords: Map<String, TokenType> =
    v1_0Keywords +
        mapOf(
            "const" to Const,
            "if" to If,
            "else" to Else,
            "readInput" to Call("readInput"),
            "readEnv" to Call("readEnv"),
            "boolean" to DataType(BoolType),
            "true" to Literal("true", BoolType),
            "false" to Literal("false", BoolType),
        )
