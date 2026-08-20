package interpreter.cases

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue
import interpreter.PrintEvent
import interpreter.RuntimeEnvironment
import interpreter.RuntimeEvents
import interpreter.VariableInfo
import java.util.Optional


val POS = Position(0, 0)


val LITERAL_NUMBER_5 = Expression.Literal(PrintScriptValue.NumberLiteral(5))
val LITERAL_NUMBER_10 = Expression.Literal(PrintScriptValue.NumberLiteral(10))
val LITERAL_NUMBER_1 = Expression.Literal(PrintScriptValue.NumberLiteral(1))
val LITERAL_NUMBER_2 = Expression.Literal(PrintScriptValue.NumberLiteral(2))

val LITERAL_STRING_HOLA = Expression.Literal(PrintScriptValue.StringLiteral("hola"))
val LITERAL_STRING_X = Expression.Literal(PrintScriptValue.StringLiteral("x"))

val VARIABLE_X = Expression.Variable("x")
val VARIABLE_A = Expression.Variable("a")

val OPERATION_1_PLUS_2 = Expression.Operation(
    left = LITERAL_NUMBER_1,
    right = LITERAL_NUMBER_2,
    operator = PrintScriptOperator.SUM
)

val OPERATION_5_DIVIDE_STRING = Expression.Operation(
    left = LITERAL_NUMBER_5,
    right = LITERAL_STRING_X,
    operator = PrintScriptOperator.DIVIDE
)

val OPERATION_X_PLUS_1 = Expression.Operation(
    left = VARIABLE_X,
    right = LITERAL_NUMBER_1,
    operator = PrintScriptOperator.SUM
)

 
val ID_X = ASTIdentifier("x")
val ID_A = ASTIdentifier("a")

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

val ASSIGNMENT_A_TO_5 = AST.Assignment(
    id = ID_A,
    value = LITERAL_NUMBER_5
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

val CALL_PRINTLN_X = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(VARIABLE_X)
)

val CALL_PRINTLN_OPERATION_1_PLUS_2 = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_1_PLUS_2)
)

val CALL_PRINTLN_UNDECLARED_A = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(VARIABLE_A)
)

val CALL_PRINTLN_OPERATION_X_PLUS_1 = AST.Call(
    functionName = PrintScriptFunctions.PRINTLN,
    args = listOf(OPERATION_X_PLUS_1)
)

  
val EMPTY_ENV = RuntimeEnvironment(emptyMap())

val ENV_WITH_X_EQUAL_TO_5 = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(5))
        )
    )
)

val ENV_WITH_X_EQUAL_TO_10 = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(10))
        )
    )
)

val ENV_WITH_X_EQUAL_TO_2 = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(2))
        )
    )
)

val ENV_WITH_X_STRING_HOLA = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.STRING,
            Optional.of(PrintScriptValue.StringLiteral("hola"))
        )
    )
)

val ENV_WITH_X_NUMBER_NO_VALUE = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.empty()
        )
    )
)

val ENV_WITH_X_STRING_NO_VALUE = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.STRING,
            Optional.empty()
        )
    )
)

val ENV_WITH_A_EQUAL_TO_1 = RuntimeEnvironment(
    mapOf(
        "a" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(1))
        )
    )
)

val ENV_WITH_A_EQUAL_TO_5 = RuntimeEnvironment(
    mapOf(
        "a" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(5))
        )
    )
)

 
val ENV_WITH_X_10_AND_A_1 = RuntimeEnvironment(
    mapOf(
        "x" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(10))
        ),
        "a" to VariableInfo(
            PrintScriptType.NUMBER,
            Optional.of(PrintScriptValue.NumberLiteral(1))
        )
    )
)

 
val EMPTY_EVENTS = RuntimeEvents(emptyList())

val EVENTS_WITH_PRINT_HOLA = RuntimeEvents(
    listOf(PrintEvent("hola"))
)

val EVENTS_WITH_PRINT_5 = RuntimeEvents(
    listOf(PrintEvent("5"))
)

val EVENTS_WITH_PRINT_3 = RuntimeEvents(
    listOf(PrintEvent("3"))
)

val EVENTS_WITH_PRINT_5_THEN_10 = RuntimeEvents(
    listOf(PrintEvent("5"), PrintEvent("10"))
)