package executor.cases

import executor.FailureCase

object FailedPrograms {
    fun cases() =
        listOf(
            FailureCase(
                "lexer error cuts off pipeline",
                "let x: number = 5@;",
            ),
            FailureCase(
                "parser error cuts off pipeline",
                "let x: = 5;",
            ),
            FailureCase(
                "runtime error cuts off pipeline on variable redeclaration",
                "let x: number = 1;\nlet x: number = 2;",
            ),
            FailureCase(
                "runtime error cuts off pipeline on type mismatch",
                "let x: number = \"text\";",
            ),
        )
}
