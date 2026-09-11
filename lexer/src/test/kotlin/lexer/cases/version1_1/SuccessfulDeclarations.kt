package lexer.cases.version1_1

import domain.BoolType
import domain.NumType
import lexer.SuccessCase
import tokens.Assign
import tokens.Colon
import tokens.Const
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.Semicolon

object SuccessfulDeclarations {
    fun cases() =
        listOf(
            SuccessCase(
                "const number declaration",
                "const x: number = 5;",
                listOf(
                    Const,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "const boolean true declaration",
                "const x: boolean = true;",
                listOf(
                    Const,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Literal("true", BoolType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "const boolean false declaration",
                "const x: boolean = false;",
                listOf(
                    Const,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Literal("false", BoolType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "let boolean true declaration",
                "let x: boolean = true;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Literal("true", BoolType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "let boolean false declaration",
                "let x: boolean = false;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Literal("false", BoolType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "let boolean with spaces declaration",
                "let x : boolean = true ;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Literal("true", BoolType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "const boolean with identifier value declaration",
                "const x: boolean = tru3;",
                listOf(
                    Const,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Identifier("tru3"),
                    Semicolon,
                ),
            ),
        )
}
