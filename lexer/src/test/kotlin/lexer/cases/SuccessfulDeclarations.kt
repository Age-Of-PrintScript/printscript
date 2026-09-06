package lexer.cases

import domain.PrintScriptType
import lexer.SuccessCase
import tokens.ASSIGNViejo
import tokens.COLONViejo
import tokens.DataTypeViejo
import tokens.IdentifierViejo
import tokens.LETViejo
import tokens.LiteralViejo
import tokens.SEMICOLONViejo

object SuccessfulDeclarations {
    fun cases() =
        listOf(
            SuccessCase(
                "number declaration",
                "let x: number = 5;",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.NUMBER),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "string declaration",
                "let x: string = \"Hello\";",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.STRING),
                    ASSIGNViejo,
                    LiteralViejo("Hello"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "empty string declaration",
                "let x: string = \"\";",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.STRING),
                    ASSIGNViejo,
                    LiteralViejo(""),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "string with spaces declaration",
                "let x: string = \"hola mundo\";",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.STRING),
                    ASSIGNViejo,
                    LiteralViejo("hola mundo"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "declaration with no spaces in input",
                "let x:number=5;",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.NUMBER),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "declaration with multiple spaces in input",
                "let    x  :   number  =   5  ;",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.NUMBER),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "multiline declaration",
                "let x: number = 5;\n " +
                    "let y: string = \"Hello\";",
                listOf(
                    LETViejo,
                    IdentifierViejo("x"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.NUMBER),
                    ASSIGNViejo,
                    LiteralViejo("5"),
                    SEMICOLONViejo,
                    LETViejo,
                    IdentifierViejo("y"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.STRING),
                    ASSIGNViejo,
                    LiteralViejo("Hello"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "declaration with identifier value",
                "let x1: number = x2;",
                listOf(
                    LETViejo,
                    IdentifierViejo("x1"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.NUMBER),
                    ASSIGNViejo,
                    IdentifierViejo("x2"),
                    SEMICOLONViejo,
                ),
            ),
            SuccessCase(
                "declaration with string with single quote inside",
                "let x1: string = \"let's move\";",
                listOf(
                    LETViejo,
                    IdentifierViejo("x1"),
                    COLONViejo,
                    DataTypeViejo(PrintScriptType.STRING),
                    ASSIGNViejo,
                    LiteralViejo("let's move"),
                    SEMICOLONViejo,
                ),
            ),
        )
}
