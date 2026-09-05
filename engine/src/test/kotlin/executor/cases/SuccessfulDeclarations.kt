package executor.cases

import executor.SuccessCase

object SuccessfulDeclarations {
    fun cases() =
        listOf(
            SuccessCase(
                "number declaration",
                "let x: number = 5; println(x);",
                listOf("5.0"),
            ),
            SuccessCase(
                "string declaration",
                "let x: string = \"Hello\"; println(x);",
                listOf("Hello"),
            ),
            SuccessCase(
                "empty string declaration",
                "let x: string = \"\"; println(x);",
                listOf(""),
            ),
            SuccessCase(
                "string with spaces declaration",
                "let x: string = \"hola mundo\"; println(x);",
                listOf("hola mundo"),
            ),
            SuccessCase(
                "uninitialized number declaration assigned later",
                "let x: number; x = 10; println(x);",
                listOf("10.0"),
            ),
            SuccessCase(
                "uninitialized string declaration assigned later",
                "let s: string; s = \"text\"; println(s);",
                listOf("text"),
            ),
            SuccessCase(
                "multiple declarations",
                "let x: number = 5;\nlet y: string = \"Hello\";\nprintln(x);\nprintln(y);",
                listOf("5.0", "Hello"),
            ),
            SuccessCase(
                "declaration with identifier value",
                "let x1: number = 5;\nlet x2: number = x1;\nprintln(x2);",
                listOf("5.0"),
            ),
        )
}
