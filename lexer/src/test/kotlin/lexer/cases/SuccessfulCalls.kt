package lexer.cases

import domain.NumType
import domain.StrType
import lexer.SuccessCase
import lexer.Sum
import tokens.Call
import tokens.CloseParen
import tokens.Identifier
import tokens.Literal
import tokens.OpenParen
import tokens.Operator
import tokens.Semicolon

object SuccessfulCalls {
    fun cases() =
        listOf(
            SuccessCase(
                "println call with number",
                "println(5);",
                listOf(
                    Call("println"),
                    OpenParen,
                    Literal("5", NumType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "println call with identifier",
                "println(x);",
                listOf(
                    Call("println"),
                    OpenParen,
                    Identifier("x"),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "println call with string",
                "println(\"texto\");",
                listOf(
                    Call("println"),
                    OpenParen,
                    Literal("texto", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "println call with expression",
                "println(5 + 2);",
                listOf(
                    Call("println"),
                    OpenParen,
                    Literal("5", NumType),
                    Operator(Sum),
                    Literal("2", NumType),
                    CloseParen,
                    Semicolon,
                ),
            ),
        )
}
