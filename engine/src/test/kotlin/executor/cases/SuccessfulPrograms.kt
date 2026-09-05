package executor.cases

import executor.SuccessCase

object SuccessfulPrograms {
    fun cases() =
        listOf(
            SuccessCase(
                "hello world print",
                "println(\"Hello, World!\");",
                listOf("Hello, World!"),
            ),
            SuccessCase(
                "variable declaration and arithmetic calculation",
                "let x: number = 10;\nlet y: number = 20;\nprintln(x + y);",
                listOf("30.0"),
            ),
            SuccessCase(
                "variable reassignment and operation",
                "let count: number = 1;\ncount = count + 2;\nprintln(count);",
                listOf("3.0"),
            ),
            SuccessCase(
                "string and number concatenation",
                "let label: string = \"Result: \";\nlet total: number = 42;\nprintln(label + total);",
                listOf("Result: 42.0"),
            ),
            SuccessCase(
                "multi-line complete program",
                "let a: number = 5;\n" +
                    "let b: number = 2;\n" +
                    "let msg: string = \"Total: \";\n" +
                    "println(msg + a * b);\n" +
                    "a = a + 1;\n" +
                    "println(\"Updated: \" + a);",
                listOf("Total: 10.0", "Updated: 6.0"),
            ),
        )
}
