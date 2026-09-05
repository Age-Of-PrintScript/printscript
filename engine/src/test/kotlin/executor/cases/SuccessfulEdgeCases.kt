package executor.cases

import executor.SuccessCase

object SuccessfulEdgeCases {
    fun cases() =
        listOf(
            SuccessCase(
                "program with no spaces",
                "let x:number=5;println(x);",
                listOf("5.0"),
            ),
            SuccessCase(
                "program with extra spaces and newlines",
                "let    x  :   number  =   5  ;\n\n   println(  x ) ;",
                listOf("5.0"),
            ),
            SuccessCase(
                "complex multi-line program",
                "let x: number = 10;\n" +
                    "let y: number = 20;\n" +
                    "let sum: number = x + y;\n" +
                    "println(\"Sum is: \" + sum);\n" +
                    "sum = sum * 2;\n" +
                    "println(\"Doubled sum: \" + sum);",
                listOf("Sum is: 30.0", "Doubled sum: 60.0"),
            ),
        )
}
