package executor.ps_versions.v1_1

import executor.ps_versions.v1_0.v1_0keywords
import tokens.Call
import tokens.Const
import tokens.Else
import tokens.If

val v1_1keywords =
    v1_0keywords +
        mapOf(
            "const" to Const,
            "if" to If,
            "else" to Else,
            "readInput" to Call("readInput"),
            "readEnv" to Call("readInput"),
            "boolean" to BoolType,
        )
