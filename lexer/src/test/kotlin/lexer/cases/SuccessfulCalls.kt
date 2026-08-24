package lexer.cases

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import lexer.SuccessCase
import tokens.CLOSED_PARENTHESIS
import tokens.Call
import tokens.Identifier
import tokens.Literal
import tokens.OPEN_PARENTHESIS
import tokens.Operator
import tokens.SEMICOLON

object SuccessfulCalls {
    fun cases() =
        listOf(
            SuccessCase(
                "println call with number",
                "println(5);",
                listOf(
                    Call(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESIS,
                    Literal("5"),
                    CLOSED_PARENTHESIS,
                    SEMICOLON,
                ),
            ),
            SuccessCase(
                "println call with identifier",
                "println(x);",
                listOf(
                    Call(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESIS,
                    Identifier("x"),
                    CLOSED_PARENTHESIS,
                    SEMICOLON,
                ),
            ),
            SuccessCase(
                "println call with string",
                "println(\"texto\");",
                listOf(
                    Call(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESIS,
                    Literal("texto"),
                    CLOSED_PARENTHESIS,
                    SEMICOLON,
                ),
            ),
            SuccessCase(
                "println call with expression",
                "println(5 + 2);",
                listOf(
                    Call(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESIS,
                    Literal("5"),
                    Operator(PrintScriptOperator.SUM),
                    Literal("2"),
                    CLOSED_PARENTHESIS,
                    SEMICOLON,
                ),
            ),
        )
}
