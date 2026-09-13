package lexer.cases.version1_1

import lexer.FailureCase
import lexer.LexerError

object UnterminatedStrings {
    fun cases() =
        listOf(
            FailureCase(
                "unterminated string in const declaration",
                "const x: string = \"hola;",
                LexerError.UNTERMINATED_STRING,
            ),
            FailureCase(
                "unterminated string in readInput",
                "let x: number = readInput(\"Ingrese numero: );",
                LexerError.UNTERMINATED_STRING,
            ),
            FailureCase(
                "unterminated string in readEnv",
                "let x: string = readEnv(\"PATH);",
                LexerError.UNTERMINATED_STRING,
            ),
            FailureCase(
                "unterminated string in if block",
                "if (x) { println(\"sin cerrar); }",
                LexerError.UNTERMINATED_STRING,
            ),
        )
}
