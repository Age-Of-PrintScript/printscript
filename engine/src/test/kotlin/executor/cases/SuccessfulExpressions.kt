package executor.cases

import executor.SuccessCase

object SuccessfulExpressions {
    fun cases() =
        listOf(
            SuccessCase(
                "addition expression",
                "println(5 + 2);",
                listOf("7.0"),
            ),
            SuccessCase(
                "subtraction expression",
                "println(10 - 4);",
                listOf("6.0"),
            ),
            SuccessCase(
                "multiplication expression",
                "println(3 * 4);",
                listOf("12.0"),
            ),
            SuccessCase(
                "division expression",
                "println(20 / 4);",
                listOf("5.0"),
            ),
            SuccessCase(
                "multiplication precedence over addition",
                "println(2 + 3 * 4);",
                listOf("14.0"),
            ),
            SuccessCase(
                "division precedence over subtraction",
                "println(10 - 8 / 4);",
                listOf("8.0"),
            ),
            SuccessCase(
                "parentheses forcing addition before multiplication",
                "let x: number = (2 + 3) * 4;\nprintln(x);",
                listOf("20.0"),
            ),
            SuccessCase(
                "parentheses forcing subtraction before division",
                "let x: number = (10 - 2) / 4;\nprintln(x);",
                listOf("2.0"),
            ),
            SuccessCase(
                "string concatenation",
                "println(\"hello \" + \"world\");",
                listOf("hello world"),
            ),
            SuccessCase(
                "string and number concatenation",
                "println(\"result: \" + 5);",
                listOf("result: 5.0"),
            ),
            SuccessCase(
                "number and string concatenation",
                "println(5 + \" items\");",
                listOf("5.0 items"),
            ),
            SuccessCase(
                "expression with variables",
                "let x: number = 10;\nlet y: number = 5;\nprintln(x * y - 2);",
                listOf("48.0"),
            ),
        )
}
