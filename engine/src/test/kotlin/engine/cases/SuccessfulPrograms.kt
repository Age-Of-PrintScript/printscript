package engine.cases

import engine.SuccessCase

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
                listOf("30"),
            ),
            SuccessCase(
                "variable reassignment and operation",
                "let count: number = 1;\ncount = count + 2;\nprintln(count);",
                listOf("3"),
            ),
            SuccessCase(
                "string and number concatenation",
                "let label: string = \"Result: \";\nlet total: number = 42;\nprintln(label + total);",
                listOf("Result: 42"),
            ),
            SuccessCase(
                "multi-line complete program",
                "let a: number = 5;\n" +
                    "let b: number = 2;\n" +
                    "let msg: string = \"Total: \";\n" +
                    "println(msg + a * b);\n" +
                    "a = a + 1;\n" +
                    "println(\"Updated: \" + a);",
                listOf("Total: 10", "Updated: 6"),
            ),
            // Version 1.1 Cases
            SuccessCase(
                "v1.1 const declaration and usage",
                "const x: number = 10;\nprintln(x + 5);",
                listOf("15"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 boolean declaration and printing",
                "let isActive: boolean = true;\nconst isDone: boolean = false;\nprintln(isActive);\nprintln(isDone);",
                listOf("true", "false"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 if statement when condition is true",
                "let condition: boolean = true;\nif (condition) {\nprintln(\"Condition was true\");\n}",
                listOf("Condition was true"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 if statement when condition is false skips block",
                "let condition: boolean = false;\nif (condition) {\nprintln(\"Should not print\");\n}\nprintln(\"After if\");",
                listOf("After if"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 if-else statement executing if branch",
                "if (true) {\nprintln(\"If branch\");\n} else {\nprintln(\"Else branch\");\n}",
                listOf("If branch"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 if-else statement executing else branch",
                "if (false) {\nprintln(\"If branch\");\n} else {\nprintln(\"Else branch\");\n}",
                listOf("Else branch"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 variable mutation inside if block affects outer scope",
                "let count: number = 0;\nif (true) {\ncount = count + 10;\n}\nprintln(count);",
                listOf("10"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 nested if-else statements",
                "let outer: boolean = true;\nlet inner: boolean = false;\nif (outer) {\nif (inner) {\nprintln(\"Inner true\");\n} else {\nprintln(\"Inner false\");\n}\n}",
                listOf("Inner false"),
                version = "1.1",
            ),
            SuccessCase(
                "v1.1 readInput returns string",
                "let name: string = readInput(\"Enter name: \");\nprintln(\"Hello \" + name);",
                listOf("Hello Alice"),
                version = "1.1",
                inputs = listOf("Alice"),
            ),
            SuccessCase(
                "v1.1 readInput implicit cast to number",
                "let age: number = readInput(\"Enter age: \");\nprintln(age + 5);",
                listOf("30"),
                version = "1.1",
                inputs = listOf("25"),
            ),
            SuccessCase(
                "v1.1 readInput implicit cast to boolean",
                "let isSubscribed: boolean = readInput(\"Is subscribed: \");\nprintln(isSubscribed);",
                listOf("true"),
                version = "1.1",
                inputs = listOf("true"),
            ),
            SuccessCase(
                "v1.1 readEnv reading existing env variable",
                "let host: string = readEnv(\"DB_HOST\");\nprintln(host);",
                listOf("localhost"),
                version = "1.1",
                env = mapOf("DB_HOST" to "localhost"),
            ),
            SuccessCase(
                "v1.1 readEnv with implicit cast to number",
                "let port: number = readEnv(\"PORT\");\nprintln(port + 1);",
                listOf("8081"),
                version = "1.1",
                env = mapOf("PORT" to "8080"),
            ),
            SuccessCase(
                "v1.1 backwards compatibility with v1.0 code",
                "let a: number = 5;\nlet b: number = 2;\nprintln(a * b);",
                listOf("10"),
                version = "1.1",
            ),
        )
}
