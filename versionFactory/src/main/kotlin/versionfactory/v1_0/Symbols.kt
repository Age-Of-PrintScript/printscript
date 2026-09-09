package versionfactory.v1_0

import tokens.Assign
import tokens.CloseParen
import tokens.Colon
import tokens.OpenParen
import tokens.Operator
import tokens.Semicolon
import tokens.TokenType

internal val v1_0Symbols: Map<Char, TokenType> =
    mapOf(
        '+' to Operator(Operators.SUM),
        '-' to Operator(Operators.SUBTRACT),
        '*' to Operator(Operators.MULTIPLY),
        '/' to Operator(Operators.DIVIDE),
        ':' to Colon,
        ';' to Semicolon,
        '=' to Assign,
        '(' to OpenParen,
        ')' to CloseParen,
    )
