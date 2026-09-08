package interpreter.cases

import ast.Expression
import domain.NumType
import domain.StrType
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.VariableInfo

val EMPTY_ENV = RuntimeEnvironment(emptyMap())

val ENV_WITH_X_EQUAL_TO_5 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    Expression.Literal("5", NumType),
                ),
        ),
    )

val ENV_WITH_X_EQUAL_TO_10 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    Expression.Literal("10", NumType),
                ),
        ),
    )

val ENV_WITH_X_EQUAL_TO_2 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    Expression.Literal("2", NumType),
                ),
        ),
    )

val ENV_WITH_X_STRING_HOLA =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    StrType,
                    Expression.Literal("hola", StrType),
                ),
        ),
    )

val ENV_WITH_X_NUMBER_NO_VALUE =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    null,
                ),
        ),
    )

val ENV_WITH_X_STRING_NO_VALUE =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    StrType,
                    null,
                ),
        ),
    )

val ENV_WITH_A_EQUAL_TO_1 =
    RuntimeEnvironment(
        mapOf(
            "a" to
                VariableInfo(
                    NumType,
                    Expression.Literal("1", NumType),
                ),
        ),
    )

val ENV_WITH_X_5_AND_A_1 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    Expression.Literal("5", NumType),
                ),
            "a" to
                VariableInfo(
                    NumType,
                    Expression.Literal("1", NumType),
                ),
        ),
    )

val ENV_WITH_X_5_AND_Y_5 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    NumType,
                    Expression.Literal("5", NumType),
                ),
            "y" to
                VariableInfo(
                    NumType,
                    Expression.Literal("5", NumType),
                ),
        ),
    )
