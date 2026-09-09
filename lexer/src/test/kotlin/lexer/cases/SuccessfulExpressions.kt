package lexer.cases

import domain.NumType
import lexer.Divide
import lexer.Multiply
import lexer.Subtract
import lexer.SuccessCase
import lexer.Sum
import tokens.Assign
import tokens.CloseParen
import tokens.Identifier
import tokens.Literal
import tokens.OpenParen
import tokens.Operator
import tokens.Semicolon

object SuccessfulExpressions {
    fun cases() =
        listOf(
            SuccessCase(
                "number assignment with expression",
                "x = 5 + 2;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("5", NumType),
                    Operator(Sum),
                    Literal("2", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "parenthesized expression with multiply",
                "x = (5 + 2) * 3;",
                listOf(
                    Identifier("x"),
                    Assign,
                    OpenParen,
                    Literal("5", NumType),
                    Operator(Sum),
                    Literal("2", NumType),
                    CloseParen,
                    Operator(Multiply),
                    Literal("3", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "expression with all operators",
                "x = 5 + 2 - 3 * 4 / 2;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("5", NumType),
                    Operator(Sum),
                    Literal("2", NumType),
                    Operator(Subtract),
                    Literal("3", NumType),
                    Operator(Multiply),
                    Literal("4", NumType),
                    Operator(Divide),
                    Literal("2", NumType),
                    Semicolon,
                ),
            ),
        )
}
