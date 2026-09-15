package engine

import engine.cases.ValidationCases
import org.junit.jupiter.api.Assertions.assertEquals

class TestLogger : Logger {
    val logs = mutableListOf<String>()

    override fun log(string: String) {
        logs.add(string)
    }

    fun getPrints(): List<String> = if (logs.isNotEmpty()) logs.dropLast(1) else emptyList()
}

internal fun assertCorrectExecution(
    engine: Engine,
    case: SuccessCase,
) {
    val logger = TestLogger()
    val prints = mutableListOf<String>()
    val inputIterator = case.inputs.iterator()
    val io =
        EngineIO(
            emitter = { prints.add(it) },
            provider = { if (inputIterator.hasNext()) inputIterator.next() else "" },
            envProvider = { case.env[it] },
        )
    val result = engine.execute(case.input, io, logger, version = case.version)
    assertEquals(ExitCode.SUCCESS, result.exitCode, "Execution was expected to succeed")
    assertEquals(case.expectedOutputs, prints)
    assertEquals(listOf("Build Successful"), logger.logs.takeLast(1))
}

internal fun assertFailedExecution(
    engine: Engine,
    case: FailureCase,
) {
    val logger = TestLogger()
    val inputIterator = case.inputs.iterator()
    val io =
        EngineIO(
            emitter = {},
            provider = { if (inputIterator.hasNext()) inputIterator.next() else "" },
            envProvider = { case.env[it] },
        )
    val result = engine.execute(case.input, io, logger, version = case.version)
    assertEquals(ExitCode.FAILURE, result.exitCode, "Execution was expected to fail")
    assertEquals("Build Failed", logger.logs.lastOrNull())
}

internal fun assertCorrectValidation(
    engine: Engine,
    case: ValidationCases.Case,
) {
    val logger = TestLogger()
    val result = engine.validate(case.input, logger, version = case.version)
    assertEquals(ExitCode.SUCCESS, result, "Validation was expected to succeed")
    assertEquals(listOf("Validation Successful"), logger.logs)
}

internal fun assertFailedValidation(
    engine: Engine,
    case: ValidationCases.Case,
) {
    val logger = TestLogger()
    val result = engine.validate(case.input, logger, version = case.version)
    assertEquals(ExitCode.FAILURE, result, "Validation was expected to fail")
    assertEquals("Build Failed", logger.logs.lastOrNull())
}
