package lexer.cases

import domain.PrintScriptOperator
import domain.PrintScriptValue
import lexer.SuccessCase
import tokens.ASSIGN
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.SEMICOLON


object SuccessfulAssignments {
    fun cases() = listOf(
        SuccessCase(
            "number assignment",
            "x = 5;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string that looks like number",
            "x = \"5\";",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("5")),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string that contains symbols",
            "x = \"hola mundo;\";",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("hola mundo;")),
                SEMICOLON
            )
        ),
        SuccessCase(
            "number with 0s in the left",
            "x = 000123;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(123.0)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "number = 0",
            "x = 0;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(0)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "numbers in identifier",
            "m1Variable1234 = 0;",
            listOf(
                Identifier("m1Variable1234"),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(0)),
                SEMICOLON
            )
        )

    )
}