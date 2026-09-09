package versionfactory.v1_1

import tokens.CloseBraces
import tokens.OpenBraces
import tokens.TokenType
import versionfactory.v1_0.v1_0Symbols

internal val v1_1Symbols: Map<Char, TokenType> =
    v1_0Symbols +
        mapOf(
            '{' to OpenBraces,
            '}' to CloseBraces,
        )
