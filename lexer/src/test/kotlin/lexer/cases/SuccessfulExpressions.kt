package lexer.cases

import domain.PrintScriptOperator
import domain.PrintScriptValue
import lexer.SuccessCase
import tokens.ASSIGN
import tokens.ClosedParenthesis
import tokens.Identifier
import tokens.Literal
import tokens.OpenParenthesis
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
                Literal(PrintScriptValue.NumberLiteral(5)),
                Operator(PrintScriptOperator.SUM),
                Literal(PrintScriptValue.NumberLiteral(2)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "parenthesized expression with multiply",
            "x = (5 + 2) * 3;",
            listOf(
                Identifier("x"),
                ASSIGN,
                OpenParenthesis,
                Literal(PrintScriptValue.NumberLiteral(5)),
                Operator(PrintScriptOperator.SUM),
                Literal(PrintScriptValue.NumberLiteral(2)),
                ClosedParenthesis,
                Operator(PrintScriptOperator.MULTIPLY),
                Literal(PrintScriptValue.NumberLiteral(3)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "expression with all operators",
            "x = 5 + 2 - 3 * 4 / 2;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                Operator(PrintScriptOperator.SUM),
                Literal(PrintScriptValue.NumberLiteral(2)),
                Operator(PrintScriptOperator.SUBTRACT),
                Literal(PrintScriptValue.NumberLiteral(3)),
                Operator(PrintScriptOperator.MULTIPLY),
                Literal(PrintScriptValue.NumberLiteral(4)),
                Operator(PrintScriptOperator.DIVIDE),
                Literal(PrintScriptValue.NumberLiteral(2)),
                SEMICOLON
            )
        )
    )
}