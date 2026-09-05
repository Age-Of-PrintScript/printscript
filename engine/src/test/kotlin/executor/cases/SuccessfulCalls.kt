package executor.cases

import executor.SuccessCase

object SuccessfulCalls {
    fun cases() =
        listOf(
            SuccessCase(
                "println call with number literal",
                "println(5);",
                listOf("5.0"),
            ),
            SuccessCase(
                "println call with decimal number literal",
                "println(3.14);",
                listOf("3.14"),
            ),
            SuccessCase(
                "println call with string literal",
                "println(\"hello\");",
                listOf("hello"),
            ),
            SuccessCase(
                "println call with identifier",
                "let x: number = 42; println(x);",
                listOf("42.0"),
            ),
            SuccessCase(
                "println call with expression",
                "println(5 + 2);",
                listOf("7.0"),
            ),
            SuccessCase(
                "multiple println calls",
                "println(\"first\");\nprintln(\"second\");\nprintln(\"third\");",
                listOf("first", "second", "third"),
            ),
        )
}
