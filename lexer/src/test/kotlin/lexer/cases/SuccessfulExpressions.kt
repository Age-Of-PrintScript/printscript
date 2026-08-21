package lexer.cases

import domain.PrintScriptOperator
import domain.PrintScriptValue
import lexer.SuccessCase
import tokens.ASSIGN
import tokens.CLOSED_PARENTHESIS
import tokens.Identifier
import tokens.Literal
import tokens.OPEN_PARENTHESIS
import tokens.Operator
import tokens.SEMICOLON

object SuccessfulExpressions {
    fun cases() = listOf(
        SuccessCase(
            "number assignment with expression",
            "x = 5 + 2;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("5"),
                Operator(PrintScriptOperator.SUM),
                Literal("2"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "parenthesized expression with multiply",
            "x = (5 + 2) * 3;",
            listOf(
                Identifier("x"),
                ASSIGN,
                OPEN_PARENTHESIS,
                Literal("5"),
                Operator(PrintScriptOperator.SUM),
                Literal("2"),
                CLOSED_PARENTHESIS,
                Operator(PrintScriptOperator.MULTIPLY),
                Literal("3"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "expression with all operators",
            "x = 5 + 2 - 3 * 4 / 2;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("5"),
                Operator(PrintScriptOperator.SUM),
                Literal("2"),
                Operator(PrintScriptOperator.SUBTRACT),
                Literal("3"),
                Operator(PrintScriptOperator.MULTIPLY),
                Literal("4"),
                Operator(PrintScriptOperator.DIVIDE),
                Literal("2"),
                SEMICOLON
            )
        )
    )
}
