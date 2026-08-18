package lexer.cases

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptValue
import lexer.SuccessCase
import tokens.Call
import tokens.CLOSED_PARENTHESIS
import tokens.Identifier
import tokens.Literal
import tokens.OPEN_PARENTHESIS
import tokens.Operator
import tokens.SEMICOLON


object SuccessfulCalls {
    fun cases() = listOf(
        SuccessCase(
            "println call with number",
            "println(5);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OPEN_PARENTHESIS,
                Literal(PrintScriptValue.NumberLiteral(5)),
                CLOSED_PARENTHESIS,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with identifier",
            "println(x);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OPEN_PARENTHESIS,
                Identifier("x"),
                CLOSED_PARENTHESIS,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with string",
            "println(\"texto\");",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OPEN_PARENTHESIS,
                Literal(PrintScriptValue.StringLiteral("texto")),
                CLOSED_PARENTHESIS,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with expression",
            "println(5 + 2);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OPEN_PARENTHESIS,
                Literal(PrintScriptValue.NumberLiteral(5)),
                Operator(PrintScriptOperator.SUM),
                Literal(PrintScriptValue.NumberLiteral(2)),
                CLOSED_PARENTHESIS,
                SEMICOLON
            )
        )
    )
}