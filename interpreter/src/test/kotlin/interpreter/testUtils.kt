package interpreter

import domain.Failure
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import junit.framework.TestCase.assertTrue
import java.util.Optional
import kotlin.test.assertEquals

internal fun assertSuccessCase(
    interpreter: Interpreter,
    case: SuccessCase
) {
    when (val result = interpreter.execute(case.program)) {
        is Success -> {
            assertEquals(case.expectedEnv, result.value.runtimeEnvironment, "RuntimeEnvironment mismatch for case: ${case.name}")
            assertEquals(case.expectedEvents, result.value.runtimeEvents, "RuntimeEvents mismatch for case: ${case.name}")
        }
        is Failure -> {
            throw AssertionError("Expected success for case '${case.name}', but failed with: ${result.value}")
        }
    }
}

internal fun assertFailureCase(
    interpreter: Interpreter,
    case: FailureCase
) {
    when (val result = interpreter.execute(case.program)) {
        is Success -> {
            throw AssertionError("Expected failure with error '${case.expectedFailure}' for case '${case.name}', but succeeded with: ${result.value}")
        }
        is Failure -> {
            assertEquals(case.expectedFailure, result.value, "Error mismatch for case: ${case.name}")
        }
    }
}