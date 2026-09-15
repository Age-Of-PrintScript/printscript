package engine.cases

import engine.FailureCase

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
            // Version 1.1 Cases
            FailureCase(
                "v1.1 runtime error on reassignment to const variable",
                "const x: number = 10;\nx = 20;",
                version = "1.1",
            ),
            FailureCase(
                "v1.1 runtime error on boolean type mismatch in declaration",
                "let flag: boolean = 123;",
                version = "1.1",
            ),
            FailureCase(
                "v1.1 runtime error on boolean type mismatch in reassignment",
                "let flag: boolean = true;\nflag = \"false\";",
                version = "1.1",
            ),
            FailureCase(
                "v1.1 runtime error when if condition is not boolean",
                "if (42) {\nprintln(\"Invalid\");\n}",
                version = "1.1",
            ),
            FailureCase(
                "v1.1 runtime error on readInput invalid cast to number",
                "let age: number = readInput(\"Age: \");",
                version = "1.1",
                inputs = listOf("not-a-number"),
            ),
            FailureCase(
                "v1.1 runtime error on readInput invalid cast to boolean",
                "let flag: boolean = readInput(\"Flag: \");",
                version = "1.1",
                inputs = listOf("not-a-boolean"),
            ),
            FailureCase(
                "v1.1 runtime error on missing environment variable",
                "let secret: string = readEnv(\"NON_EXISTENT_VAR\");",
                version = "1.1",
            ),
            FailureCase(
                "v1.1 runtime error on readEnv without arguments",
                "let secret: string = readEnv();",
                version = "1.1",
            ),
            FailureCase(
                "v1.0 fails on v1.1 const keyword",
                "const x: number = 10;",
                version = "1.0",
            ),
            FailureCase(
                "v1.0 fails on v1.1 if keyword",
                "if (true) {\nprintln(1);\n}",
                version = "1.0",
            ),
        )
}
