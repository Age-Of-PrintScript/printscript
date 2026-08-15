package lexer.cases

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue
import lexer.LexerError
import lexer.SuccessCase
import tokens.ASSIGN
import tokens.COLON
import tokens.Call
import tokens.ClosedParenthesis
import tokens.DataType
import tokens.Identifier
import tokens.LET
import tokens.Literal
import tokens.OpenParenthesis
import tokens.Operator
import tokens.SEMICOLON
import tokens.TokenType


object SuccessfulCalls {
    fun cases() = listOf(
        SuccessCase(
            "println call with number",
            "println(5);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OpenParenthesis,
                Literal(PrintScriptValue.NumberLiteral(5)),
                ClosedParenthesis,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with identifier",
            "println(x);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OpenParenthesis,
                Identifier("x"),
                ClosedParenthesis,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with string",
            "println(\"texto\");",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OpenParenthesis,
                Literal(PrintScriptValue.StringLiteral("texto")),
                ClosedParenthesis,
                SEMICOLON
            )
        ),
        SuccessCase(
            "println call with expression",
            "println(5 + 2);",
            listOf(
                Call(PrintScriptFunctions.PRINTLN),
                OpenParenthesis,
                Literal(PrintScriptValue.NumberLiteral(5)),
                Operator(PrintScriptOperator.SUM),
                Literal(PrintScriptValue.NumberLiteral(2)),
                ClosedParenthesis,
                SEMICOLON
            )
        )
    )
}