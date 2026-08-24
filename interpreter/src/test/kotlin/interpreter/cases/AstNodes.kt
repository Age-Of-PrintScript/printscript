package interpreter.cases

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptType

val POS = Position(0, 0)

val ID_X = ASTIdentifier("x")
val ID_A = ASTIdentifier("a")
val ID_Y = ASTIdentifier("y")

val TYPE_NUMBER = ASTDataType(PrintScriptType.NUMBER)
val TYPE_STRING = ASTDataType(PrintScriptType.STRING)

val DECLARATION_X_NUMBER_5 = AST.Declaration(
    id = ID_X,
    type = TYPE_NUMBER,
    value = LITERAL_NUMBER_5
)

val DECLARATION_X_STRING_HOLA = AST.Declaration(
    id = ID_X,
    type = TYPE_STRING,
    value = LITERAL_STRING_HOLA
)

val DECLARATION_X_NUMBER_NO_VALUE = AST.Declaration(
    id = ID_X,
    type = TYPE_NUMBER,
    value = null
)

val DECLARATION_X_STRING_NO_VALUE = AST.Declaration(
    id = ID_X,
    type = TYPE_STRING,
    value = null
)

val DECLARATION_A_NUMBER_1 = AST.Declaration(
    id = ID_A,
    type = TYPE_NUMBER,
    value = LITERAL_NUMBER_1
)

val DECLARATION_Y_NUMBER_NO_VALUE = AST.Declaration(
    id = ID_Y,
    type = TYPE_NUMBER,
    value = null
)

val DECLARATION_Y_NUMBER_WITH_X_VALUE = AST.Declaration(
    id = ID_Y,
    type = TYPE_NUMBER,
    value = VARIABLE_X
)

val DECLARATION_X_NUMBER_WITH_STRING_VALUE = AST.Declaration(
    id = ID_X,
    type = TYPE_NUMBER,
    value = LITERAL_STRING_X
)

val DECLARATION_X_NUMBER_WITH_INVALID_OPERATION = AST.Declaration(
    id = ID_X,
    type = TYPE_NUMBER,
    value = OPERATION_5_DIVIDE_STRING
)

val ASSIGNMENT_X_TO_10 = AST.Assignment(
    id = ID_X,
    value = LITERAL_NUMBER_10
)

val ASSIGNMENT_X_TO_2 = AST.Assignment(
    id = ID_X,
    value = LITERAL_NUMBER_2
)

val ASSIGNMENT_Y_TO_X = AST.Assignment(
    id = ID_Y,
    value = VARIABLE_X
)

val ASSIGNMENT_X_TO_STRING_X = AST.Assignment(
    id = ID_X,
    value = LITERAL_STRING_X
)

val ASSIGNMENT_UNDECLARED_A_TO_5 = AST.Assignment(
    id = ID_A,
    value = LITERAL_NUMBER_5
)

val CALL_PRINTLN_HOLA = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(LITERAL_STRING_HOLA)
)

val CALL_PRINTLN_MUNDO = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(LITERAL_STRING_MUNDO)
)

val CALL_PRINTLN_X = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(VARIABLE_X)
)

val CALL_PRINTLN_OPERATION_1_PLUS_2 = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_1_PLUS_2)
)

val CALL_PRINTLN_STRING_PLUS_NUMBER = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_STRING_PLUS_NUMBER)
)

val CALL_PRINTLN_NUMBER_PLUS_STRING = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_NUMBER_PLUS_STRING)
)

val CALL_PRINTLN_STRING_PLUS_STRING = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_STRING_PLUS_STRING)
)

val CALL_PRINTLN_UNDECLARED_A = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(VARIABLE_A)
)

val CALL_PRINTLN_OPERATION_X_PLUS_1 = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_X_PLUS_1)
)
