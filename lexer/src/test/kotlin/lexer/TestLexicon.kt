package lexer

import domain.BoolType
import domain.NumType
import domain.PSOperator
import domain.StrType
import tokens.Assign
import tokens.Call
import tokens.CloseBraces
import tokens.CloseParen
import tokens.Colon
import tokens.Const
import tokens.DataType
import tokens.Else
import tokens.If
import tokens.Let
import tokens.Literal
import tokens.OpenBraces
import tokens.OpenParen
import tokens.Operator
import tokens.Semicolon
import tokens.TokenType

internal object Sum : PSOperator {
    override val symbol = "+"
    override val precedence = 1
}

internal object Subtract : PSOperator {
    override val symbol = "-"
    override val precedence = 1
}

internal object Multiply : PSOperator {
    override val symbol = "*"
    override val precedence = 2
}

internal object Divide : PSOperator {
    override val symbol = "/"
    override val precedence = 2
}

internal val testSymbolsV1_0: Map<Char, TokenType> =
    mapOf(
        '+' to Operator(Sum),
        '-' to Operator(Subtract),
        '*' to Operator(Multiply),
        '/' to Operator(Divide),
        ':' to Colon,
        ';' to Semicolon,
        '=' to Assign,
        '(' to OpenParen,
        ')' to CloseParen,
    )

internal val testKeywordsV1_0: Map<String, TokenType> =
    mapOf(
        "let" to Let,
        "println" to Call("println"),
        "number" to DataType(NumType),
        "string" to DataType(StrType),
    )

internal val testSymbolsV1_1: Map<Char, TokenType> =
    testSymbolsV1_0 +
        mapOf(
            '{' to OpenBraces,
            '}' to CloseBraces,
        )

internal val testKeywordsV1_1: Map<String, TokenType> =
    testKeywordsV1_0 +
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
