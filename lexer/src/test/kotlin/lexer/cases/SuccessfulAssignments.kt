package lexer.cases

import lexer.SuccessCase
import tokens.ASSIGNViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.SEMICOLONViejo

object SuccessfulAssignments {
    fun cases() =
        listOf(
            SuccessCase(
                "number assignment",
                "x = 5;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "string that looks like number",
                "x = \"5\";",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "string that contains symbols",
                "x = \"hola mundo;\";",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("hola mundo;"),
                    SEMICOLONViejo,
                ),
            ),
            // ahora el lexer no lo va a pasar a 123, eso lo va a hacer el parser
            SuccessCase(
                "number with 0s in the left",
                "x = 000123;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("000123"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "number = 0",
                "x = 0;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("0"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "numbers in identifier",
                "m1Variable1234 = 0;",
                listOf(
                    IdentifierViejo("m1Variable1234"),
                    ASSIGNViejo,
                    LiteralViejo("0"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "number with decimal points",
                "x = 5.5;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("5.5"),
                    SEMICOLONViejo,
                ),
            ),
        )
}
