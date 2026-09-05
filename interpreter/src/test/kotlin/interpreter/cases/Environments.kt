package interpreter.cases

import domain.PrintScriptType
import domain.PrintScriptValue
import interpreter.RuntimeEnvironment
import interpreter.VariableInfo
import java.util.Optional

val EMPTY_ENV = RuntimeEnvironment(emptyMap())

val ENV_WITH_X_EQUAL_TO_5 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(5)),
                ),
        ),
    )

val ENV_WITH_X_EQUAL_TO_10 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(10)),
                ),
        ),
    )

val ENV_WITH_X_EQUAL_TO_2 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(2)),
                ),
        ),
    )

val ENV_WITH_X_STRING_HOLA =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.STRING,
                    Optional.of(PrintScriptValue.StringLiteral("hola")),
                ),
        ),
    )

val ENV_WITH_X_NUMBER_NO_VALUE =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.empty(),
                ),
        ),
    )

val ENV_WITH_X_STRING_NO_VALUE =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.STRING,
                    Optional.empty(),
                ),
        ),
    )

val ENV_WITH_A_EQUAL_TO_1 =
    RuntimeEnvironment(
        mapOf(
            "a" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(1)),
                ),
        ),
    )

val ENV_WITH_X_5_AND_A_1 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(5)),
                ),
            "a" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(1)),
                ),
        ),
    )

val ENV_WITH_X_5_AND_Y_5 =
    RuntimeEnvironment(
        mapOf(
            "x" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(5)),
                ),
            "y" to
                VariableInfo(
                    PrintScriptType.NUMBER,
                    Optional.of(PrintScriptValue.NumberLiteral(5)),
                ),
        ),
    )
