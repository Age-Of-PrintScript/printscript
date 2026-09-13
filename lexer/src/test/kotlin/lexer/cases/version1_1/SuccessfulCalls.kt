package lexer.cases.version1_1

import domain.BoolType
import domain.NumType
import domain.StrType
import lexer.SuccessCase
import tokens.Assign
import tokens.Call
import tokens.CloseParen
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.OpenParen
import tokens.Semicolon

object SuccessfulCalls {
    fun cases() =
        listOf(
            SuccessCase(
                "number declaration with readInput",
                "let x: number = readInput(\"Ingrese numero: \");",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Call("readInput"),
                    OpenParen,
                    Literal("Ingrese numero: ", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string declaration with readInput",
                "let x: string = readInput(\"Ingrese texto: \");",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Call("readInput"),
                    OpenParen,
                    Literal("Ingrese texto: ", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "boolean declaration with readInput",
                "let x: boolean = readInput(\"Ingrese booleano: \");",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(BoolType),
                    Assign,
                    Call("readInput"),
                    OpenParen,
                    Literal("Ingrese booleano: ", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "assignment with readInput",
                "x = readInput(\"Mensaje\");",
                listOf(
                    Identifier("x"),
                    Assign,
                    Call("readInput"),
                    OpenParen,
                    Literal("Mensaje", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "println with nested readInput",
                "println(readInput(\"Ingrese algo: \"));",
                listOf(
                    Call("println"),
                    OpenParen,
                    Call("readInput"),
                    OpenParen,
                    Literal("Ingrese algo: ", StrType),
                    CloseParen,
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "number declaration with readEnv",
                "let x: number = readEnv(\"PATH\");",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(NumType),
                    Assign,
                    Call("readEnv"),
                    OpenParen,
                    Literal("PATH", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "string declaration with readEnv",
                "let x: string = readEnv(\"HOME\");",
                listOf(
                    Let,
                    Identifier("x"),
                    Colon,
                    DataType(StrType),
                    Assign,
                    Call("readEnv"),
                    OpenParen,
                    Literal("HOME", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "assignment with readEnv",
                "x = readEnv(\"MY_VAR\");",
                listOf(
                    Identifier("x"),
                    Assign,
                    Call("readEnv"),
                    OpenParen,
                    Literal("MY_VAR", StrType),
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "println with nested readEnv",
                "println(readEnv(\"MY_VAR\"));",
                listOf(
                    Call("println"),
                    OpenParen,
                    Call("readEnv"),
                    OpenParen,
                    Literal("MY_VAR", StrType),
                    CloseParen,
                    CloseParen,
                    Semicolon,
                ),
            ),
            SuccessCase(
                "readInput call without args and semicolon",
                "readInput()",
                listOf(
                    Call("readInput"),
                    OpenParen,
                    CloseParen,
                ),
            ),
            SuccessCase(
                "readEnv call without args with semicolon",
                "readEnv();",
                listOf(
                    Call("readEnv"),
                    OpenParen,
                    CloseParen,
                    Semicolon,
                ),
            ),
        )
}
