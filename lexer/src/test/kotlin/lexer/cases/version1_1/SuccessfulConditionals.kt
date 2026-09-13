package lexer.cases.version1_1

import domain.NumType
import lexer.SuccessCase
import tokens.Assign
import tokens.Call
import tokens.CloseBraces
import tokens.CloseParen
import tokens.Colon
import tokens.DataType
import tokens.Else
import tokens.Identifier
import tokens.If
import tokens.Let
import tokens.Literal
import tokens.OpenBraces
import tokens.OpenParen
import tokens.Semicolon

object SuccessfulConditionals {
    fun cases() =
        listOf(
            SuccessCase(
                "if block with println",
                "if (x) { println(1); }",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "if block with no spaces",
                "if(x){println(1);}",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "if else block",
                "if (x) { println(1); } else { println(2); }",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                    Else,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("2", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "braces block",
                "{ let x: number = 5; }",
                listOf(
                    OpenBraces,
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "single close brace",
                "}",
                listOf(
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "single open brace",
                "{",
                listOf(
                    OpenBraces,
                ),
            ),
            SuccessCase(
                "unclosed if block",
                "if (x) { println(1);",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "if without braces",
                "if (x) println(1);",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "standalone else block",
                "else { println(2); }",
                listOf(
                    Else,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("2", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "if without parens",
                "if x { println(1); }",
                listOf(
                    If,
                    Identifier("x"),
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
            SuccessCase(
                "if else if block",
                "if (x) { println(1); } else if (y) { println(2); }",
                listOf(
                    If,
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("1", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                    Else,
                    If,
                    OpenParen,
                    Identifier("y"),
                    CloseParen,
                    OpenBraces,
                    Call("println"),
                    OpenParen,
                    Literal("2", NumType),
                    CloseParen,
                    Semicolon,
                    CloseBraces,
                ),
            ),
        )
}
