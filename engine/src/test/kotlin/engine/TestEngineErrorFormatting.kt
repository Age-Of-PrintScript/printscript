package engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestEngineErrorFormatting {
    private val engine = Engine()
    private val dummyIO =
        EngineIO(
            emitter = {},
            provider = { "" },
        )

    @Test
    fun `lexer error formats log with position`() {
        val logger = TestLogger()
        val source = "let x: number = 5@;"
        val result = engine.execute(source, dummyIO, logger)
        assertEquals(ExitCode.FAILURE, result.exitCode)
        assertEquals(2, logger.logs.size)
        assertEquals("[1:18] Invalid character", logger.logs[0])
        assertEquals("Build Failed", logger.logs[1])
    }

    @Test
    fun `parser error formats log with position`() {
        val logger = TestLogger()
        val source = "let x: = 5;"
        val result = engine.execute(source, dummyIO, logger)
        assertEquals(ExitCode.FAILURE, result.exitCode)
        assertEquals(2, logger.logs.size)
        assertEquals("[1:8] Missing type in declaration", logger.logs[0])
        assertEquals("Build Failed", logger.logs[1])
    }

    @Test
    fun `runtime error formats log with AST position`() {
        val logger = TestLogger()
        val source = "let a: number = 1;\nlet b: number = 2;\nprintln(undeclaredVar);"
        val result = engine.execute(source, dummyIO, logger)
        assertEquals(ExitCode.FAILURE, result.exitCode)
        assertEquals(2, logger.logs.size)
        assertEquals("[3:1] Variable doesn't exist", logger.logs[0])
        assertEquals("Build Failed", logger.logs[1])
    }

    @Test
    fun `validation error formats log with position`() {
        val logger = TestLogger()
        val source = "let x: = 5;"
        val result = engine.validate(source, logger)
        assertEquals(ExitCode.FAILURE, result)
        assertEquals(2, logger.logs.size)
        assertEquals("[1:8] Missing type in declaration", logger.logs[0])
        assertEquals("Build Failed", logger.logs[1])
    }
}
