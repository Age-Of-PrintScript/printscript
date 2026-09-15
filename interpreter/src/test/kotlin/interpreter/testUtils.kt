package interpreter

import domain.Failure
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals

internal fun assertSuccessCase(
    interpreter: Interpreter,
    case: SuccessCase,
) {
    val emittedPrints = mutableListOf<String>()
    val io =
        InterpreterIO(
            emitter = { emittedPrints.add(it) },
            provider = { "" },
        )
    when (val result = interpreter.execute(case.program, io, null)) {
        is Success -> {
            assertEquals(case.expectedEnv, result.value, "RuntimeEnvironment mismatch for case: ${case.name}")
            assertEquals(case.expectedEvents, emittedPrints, "RuntimeEvents mismatch for case: ${case.name}")
        }
        is Failure -> {
            throw AssertionError("Expected success for case '${case.name}', but failed with: ${result.value}")
        }
    }
}

internal fun assertFailureCase(
    interpreter: Interpreter,
    case: FailureCase,
) {
    val io =
        InterpreterIO(
            emitter = {},
            provider = { "" },
        )
    when (val result = interpreter.execute(case.program, io, null)) {
        is Success -> {
            throw AssertionError("Expected failure with error '${case.expectedFailure}' for case '${case.name}', but succeeded with: ${result.value}")
        }
        is Failure -> {
            if (case.expectedFailure.start != null) {
                assertEquals(case.expectedFailure, result.value, "Error mismatch for case: ${case.name}")
            } else {
                assertEquals(case.expectedFailure.getMessage(), result.value.getMessage(), "Error message mismatch for case: ${case.name}")
            }
        }
    }
}
