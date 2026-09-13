package lexer.cases.version1_0

import domain.NumType
import domain.StrType
import lexer.SuccessCase
import tokens.Assign
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.Semicolon

object SuccessfulDeclarations {
    fun cases() =
        listOf(
            SuccessCase(
                "number declaration",
                "let x: number = 5;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string declaration",
                "let x: string = \"Hello\";",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Literal("Hello", StrType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "empty string declaration",
                "let x: string = \"\";",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Literal("", StrType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string with spaces declaration",
                "let x: string = \"hola mundo\";",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Literal("hola mundo", StrType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "declaration with no spaces in input",
                "let x:number=5;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "declaration with multiple spaces in input",
                "let    x  :   number  =   5  ;",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "multiline declaration",
                "let x: number = 5;\n " +
                    "let y: string = \"Hello\";",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Literal("5", NumType),
                    Semicolon,
                    Let,
                    Identifier("y"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Literal("Hello", StrType),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "declaration with identifier value",
                "let x1: number = x2;",
                listOf(
                    Let,
                    Identifier("x1"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Identifier("x2"),
                    Semicolon,
                ),
            ),
            SuccessCase(
                "declaration with string with single quote inside",
                "let x1: string = \"let's move\";",
                listOf(
                    Let,
                    Identifier("x1"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Literal("let's move", StrType),
                    Semicolon,
                ),
            ),
        )
}
