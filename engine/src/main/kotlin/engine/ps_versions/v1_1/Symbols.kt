package engine.ps_versions.v1_1

import engine.ps_versions.v1_0.v1_0Symbols
import tokens.CloseBraces
import tokens.OpenBraces

val v1_1Symbols =
    v1_0Symbols +
        mapOf(
            '{' to OpenBraces,
            '}' to CloseBraces,
        )
