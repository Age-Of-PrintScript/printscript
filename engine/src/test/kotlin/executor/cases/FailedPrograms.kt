package executor.cases

import executor.FailureCase

object FailedPrograms {
    fun cases() =
        listOf(
            FailureCase(
                "invalid character in source",
                "let x: number = 5@;",
            ),
            FailureCase(
                "unterminated string literal",
                "let s: string = \"unterminated;",
            ),
            FailureCase(
                "missing semicolon",
                "let x: number = 5",
            ),
            FailureCase(
                "missing type in declaration",
                "let x: = 5;",
            ),
            FailureCase(
                "missing identifier in declaration",
                "let : number = 5;",
            ),
            FailureCase(
                "missing assignment operator in declaration",
                "let x: number 5;",
            ),
            FailureCase(
                "variable already defined",
                "let x: number = 1;\nlet x: number = 2;",
            ),
            FailureCase(
                "assignment to undeclared variable",
                "x = 10;",
            ),
            FailureCase(
                "reading undeclared variable",
                "println(x);",
            ),
            FailureCase(
                "reading uninitialized variable",
                "let x: number;\nprintln(x);",
            ),
            FailureCase(
                "type mismatch in declaration",
                "let x: number = \"hello\";",
            ),
            FailureCase(
                "type mismatch in assignment",
                "let x: number = 5;\nx = \"hello\";",
            ),
        )
}
