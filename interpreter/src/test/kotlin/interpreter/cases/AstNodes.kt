package interpreter.cases

import ast.AST
import ast.Expression
import domain.NumType
import domain.Position
import domain.StrType

val POS = Position(0, 0)

val DECLARATION_Statement_X_NUMBER_5 =
    AST.DeclarationStatement(
        id = "x",
        type = NumType,
        mutable = true,
        value = LITERAL_NUMBER_5,
    )

val DECLARATION_Statement_X_STRING_HOLA =
    AST.DeclarationStatement(
        id = "x",
        type = StrType,
        mutable = true,
        value = LITERAL_STRING_HOLA,
    )

val DECLARATION_Statement_X_NUMBER_NO_VALUE =
    AST.DeclarationStatement(
        id = "x",
        type = NumType,
        mutable = true,
        value = null,
    )

val DECLARATION_Statement_X_STRING_NO_VALUE =
    AST.DeclarationStatement(
        id = "x",
        type = StrType,
        mutable = true,
        value = null,
    )

val DECLARATION_Statement_A_NUMBER_1 =
    AST.DeclarationStatement(
        id = "a",
        type = NumType,
        mutable = true,
        value = LITERAL_NUMBER_1,
    )

val DECLARATION_Statement_Y_NUMBER_NO_VALUE =
    AST.DeclarationStatement(
        id = "y",
        type = NumType,
        mutable = true,
        value = null,
    )

val DECLARATION_Statement_Y_NUMBER_WITH_X_VALUE =
    AST.DeclarationStatement(
        id = "y",
        type = NumType,
        mutable = true,
        value = VARIABLE_X,
    )

val DECLARATION_Statement_X_NUMBER_WITH_STRING_VALUE =
    AST.DeclarationStatement(
        id = "x",
        type = NumType,
        mutable = true,
        value = LITERAL_STRING_X,
    )

val DECLARATION_Statement_X_NUMBER_WITH_INVALID_OPERATION =
    AST.DeclarationStatement(
        id = "x",
        type = NumType,
        mutable = true,
        value = OPERATION_5_DIVIDE_STRING,
    )

val ASSIGNMENT_Statement_X_TO_10 =
    AST.AssignmentStatement(
        id = "x",
        value = LITERAL_NUMBER_10,
    )

val ASSIGNMENT_Statement_X_TO_2 =
    AST.AssignmentStatement(
        id = "x",
        value = LITERAL_NUMBER_2,
    )

val ASSIGNMENT_Statement_Y_TO_X =
    AST.AssignmentStatement(
        id = "y",
        value = VARIABLE_X,
    )

val ASSIGNMENT_Statement_X_TO_STRING_X =
    AST.AssignmentStatement(
        id = "x",
        value = LITERAL_STRING_X,
    )

val ASSIGNMENT_Statement_UNDECLARED_A_TO_5 =
    AST.AssignmentStatement(
        id = "a",
        value = LITERAL_NUMBER_5,
    )

val CALL_PRINTLN_HOLA =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(LITERAL_STRING_HOLA),
        ),
    )

val CALL_PRINTLN_MUNDO =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(LITERAL_STRING_MUNDO),
        ),
    )

val CALL_PRINTLN_X =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(VARIABLE_X),
        ),
    )

val CALL_PRINTLN_OPERATION_1_PLUS_2 =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(OPERATION_1_PLUS_2),
        ),
    )

val CALL_PRINTLN_STRING_PLUS_NUMBER =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(OPERATION_STRING_PLUS_NUMBER),
        ),
    )

val CALL_PRINTLN_NUMBER_PLUS_STRING =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(OPERATION_NUMBER_PLUS_STRING),
        ),
    )

val CALL_PRINTLN_STRING_PLUS_STRING =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(OPERATION_STRING_PLUS_STRING),
        ),
    )

val CALL_PRINTLN_UNDECLARED_A =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(VARIABLE_A),
        ),
    )

val CALL_PRINTLN_OPERATION_X_PLUS_1 =
    AST.ExpressionStatement(
        Expression.Call(
            name = "println",
            args = listOf(OPERATION_X_PLUS_1),
        ),
    )
