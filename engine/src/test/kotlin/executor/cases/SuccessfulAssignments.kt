package executor.cases

import executor.SuccessCase

object SuccessfulAssignments {
    fun cases() =
        listOf(
            SuccessCase(
                "number reassignment",
                "let x: number = 5; x = 10; println(x);",
                listOf("10.0"),
            ),
            SuccessCase(
                "reassignment with another variable",
                "let a: number = 5; let b: number = 20; a = b; println(a);",
                listOf("20.0"),
            ),
            SuccessCase(
                "reassignment with expression",
                "let x: number = 2; x = x + 3; println(x);",
                listOf("5.0"),
            ),
            SuccessCase(
                "string reassignment",
                "let s: string = \"initial\"; s = \"updated\"; println(s);",
                listOf("updated"),
            ),
            SuccessCase(
                "multiple sequential reassignments",
                "let x: number = 1; x = 2; x = 3; println(x);",
                listOf("3.0"),
            ),
        )
}
