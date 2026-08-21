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
                Literal("5"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string that looks like number",
            "x = \"5\";",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("5"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string that contains symbols",
            "x = \"hola mundo;\";",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("hola mundo;"),
                SEMICOLON
            )
        ),
        // ahora el lexer no lo va a pasar a 123, eso lo va a hacer el parser
        SuccessCase(
            "number with 0s in the left",
            "x = 000123;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("000123"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "number = 0",
            "x = 0;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("0"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "numbers in identifier",
            "m1Variable1234 = 0;",
            listOf(
                Identifier("m1Variable1234"),
                ASSIGN,
                Literal("0"),
                SEMICOLON
            )
        ),
        SuccessCase(
            "number with decimal points",
            "x = 5.5;",
            listOf(
                Identifier("x"),
                ASSIGN,
                Literal("5.5"),
                SEMICOLON
            )
        )
    )
}
