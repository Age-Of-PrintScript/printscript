package lexer.cases

import domain.NumType
import domain.StrType
import lexer.SuccessCase
import tokens.Assign
import tokens.Identifier
import tokens.Literal
import tokens.Semicolon

object SuccessfulAssignments {
    fun cases() =
        listOf(
            SuccessCase(
                "number assignment",
                "x = 5;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string that looks like number",
                "x = \"5\";",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("5", StrType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string that contains symbols",
                "x = \"hola mundo;\";",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("hola mundo;", StrType),
                    Semicolon,
                ),
            ),
            // ahora el lexer no lo va a pasar a 123, eso lo va a hacer el parser
            SuccessCase(
                "number with 0s in the left",
                "x = 000123;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("000123", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "number = 0",
                "x = 0;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("0", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "numbers in identifier",
                "m1Variable1234 = 0;",
                listOf(
                    Identifier("m1Variable1234"),
                    Assign,
                    Literal("0", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "number with decimal points",
                "x = 5.5;",
                listOf(
                    Identifier("x"),
                    Assign,
                    Literal("5.5", NumType),
                    Semicolon,
                ),
            ),
        )
}
