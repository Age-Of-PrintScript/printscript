package lexer.cases

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import lexer.SuccessCase
import tokens.CLOSED_PARENTHESISViejo
import tokens.CallViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.SEMICOLONViejo

object SuccessfulCalls {
    fun cases() =
        listOf(
            SuccessCase(
                "println call with number",
                "println(5);",
                listOf(
                    CallViejo(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESISViejo,
                    LiteralViejo("5"),
                    CLOSED_PARENTHESISViejo,
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "println call with identifier",
                "println(x);",
                listOf(
                    CallViejo(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESISViejo,
                    IdentifierViejo("x"),
                    CLOSED_PARENTHESISViejo,
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "println call with string",
                "println(\"texto\");",
                listOf(
                    CallViejo(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESISViejo,
                    LiteralViejo("texto"),
                    CLOSED_PARENTHESISViejo,
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "println call with expression",
                "println(5 + 2);",
                listOf(
                    CallViejo(PrintScriptFunctions.PRINTLN),
                    OPEN_PARENTHESISViejo,
                    LiteralViejo("5"),
                    OperatorViejo(PrintScriptOperator.SUM),
                    LiteralViejo("2"),
                    CLOSED_PARENTHESISViejo,
                    SEMICOLONViejo,
                ),
            ),
        )
}
