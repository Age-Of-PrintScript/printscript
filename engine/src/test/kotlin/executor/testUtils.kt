package executor

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
    input: String,
    expectedOutputs: List<String>,
) {
    val logger = TestLogger()
    val result = engine.execute(input, logger)
    assertEquals(ExitCode.SUCCESS, result.exitCode, "Execution was expected to succeed")
    assertEquals(expectedOutputs, logger.getPrints())
    assertEquals(listOf("Build Successful"), logger.logs.takeLast(1))
}

internal fun assertFailedExecution(
    engine: Engine,
    input: String,
) {
    val logger = TestLogger()
    val result = engine.execute(input, logger)
    assertEquals(ExitCode.FAILURE, result.exitCode, "Execution was expected to fail")
    assertEquals("Build Failed", logger.logs.lastOrNull())
}
