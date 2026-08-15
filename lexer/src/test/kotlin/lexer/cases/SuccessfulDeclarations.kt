package lexer.cases

import domain.PrintScriptType
import domain.PrintScriptValue
import lexer.SuccessCase
import tokens.ASSIGN
import tokens.COLON
import tokens.DataType
import tokens.Identifier
import tokens.LET
import tokens.Literal
import tokens.SEMICOLON

object SuccessfulDeclarations {
    fun cases() = listOf(
        SuccessCase(
            "number declaration",
            "let x: number = 5;",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.NUMBER),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string declaration",
            "let x: string = \"Hello\";",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.STRING),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("Hello")),
                SEMICOLON
            )
        ),
        SuccessCase(
            "empty string declaration",
            "let x: string = \"\";",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.STRING),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("")),
                SEMICOLON
            )
        ),
        SuccessCase(
            "string with spaces declaration",
            "let x: string = \"hola mundo\";",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.STRING),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("hola mundo")),
                SEMICOLON
            )
        ),
        SuccessCase(
            "declaration with no spaces in input",
            "let x:number=5;",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.NUMBER),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "declaration with multiple spaces in input",
            "let    x  :   number  =   5  ;",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.NUMBER),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                SEMICOLON
            )
        ),
        SuccessCase(
            "multiline declaration",
            "let x: number = 5;\n " +
                    "let y: string = \"Hello\";",
            listOf(
                LET,
                Identifier("x"),
                COLON,
                DataType(PrintScriptType.NUMBER),
                ASSIGN,
                Literal(PrintScriptValue.NumberLiteral(5)),
                SEMICOLON,
                LET,
                Identifier("y"),
                COLON,
                DataType(PrintScriptType.STRING),
                ASSIGN,
                Literal(PrintScriptValue.StringLiteral("Hello")),
                SEMICOLON,
            )
        ),
        SuccessCase(
            "declaration with identifier value",
            "let x1: number = x2;",
            listOf(
                LET,
                Identifier("x1"),
                COLON,
                DataType(PrintScriptType.NUMBER),
                ASSIGN,
                Identifier("x2"),
                SEMICOLON
            )
        )
)}