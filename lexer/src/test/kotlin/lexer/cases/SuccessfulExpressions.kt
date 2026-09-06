package lexer.cases

import domain.PrintScriptOperator
import lexer.SuccessCase
import tokens.ASSIGNViejo
import tokens.CLOSED_PARENTHESISViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.SEMICOLONViejo

object SuccessfulExpressions {
    fun cases() =
        listOf(
            SuccessCase(
                "number assignment with expression",
                "x = 5 + 2;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    OperatorViejo(PrintScriptOperator.SUM),
                    LiteralViejo("2"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "parenthesized expression with multiply",
                "x = (5 + 2) * 3;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    OPEN_PARENTHESISViejo,
                    LiteralViejo("5"),
                    OperatorViejo(PrintScriptOperator.SUM),
                    LiteralViejo("2"),
                    CLOSED_PARENTHESISViejo,
                    OperatorViejo(PrintScriptOperator.MULTIPLY),
                    LiteralViejo("3"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "expression with all operators",
                "x = 5 + 2 - 3 * 4 / 2;",
                listOf(
                    IdentifierViejo("x"),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    OperatorViejo(PrintScriptOperator.SUM),
                    LiteralViejo("2"),
                    OperatorViejo(PrintScriptOperator.SUBTRACT),
                    LiteralViejo("3"),
                    OperatorViejo(PrintScriptOperator.MULTIPLY),
                    LiteralViejo("4"),
                    OperatorViejo(PrintScriptOperator.DIVIDE),
                    LiteralViejo("2"),
                    SEMICOLONViejo,
                ),
            ),
        )
}
