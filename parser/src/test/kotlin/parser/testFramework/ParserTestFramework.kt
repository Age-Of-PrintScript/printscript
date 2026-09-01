package parser.testFramework

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import parser.ParserImpl
import parser.SyntaxError
import tokens.Token
import java.io.File
import java.util.stream.Stream
import kotlin.streams.asStream
import kotlin.test.Test
import kotlin.test.assertEquals

internal data class TestCase(
    val inputTokens: List<Token>,
    val expected: Either<SyntaxError, List<AST>>,
)

internal class ParserFileTests {
    @TestFactory
    fun runAllParserTests(): Stream<DynamicTest> {
        val dir = File("src/test/resources/parserTests")
        return dir
            .listFiles()!!
            .asSequence()
            .map { file ->
                DynamicTest.dynamicTest(file.name) {
                    runOneTest(file)
                }
            }.asStream()
    }

    @Test
    fun runSingleTest() {
        runOneTest(File("src/test/resources/parserTests/case_1.md"))
    }

    private fun runOneTest(file: File) {
        val testCase = parseTestFile(file.readText())
        val actual = ParserImpl().parse(testCase.inputTokens)
        val actualTrees: Either<SyntaxError, List<AST>> =
            when (actual) {
                is Success -> Success(actual.value.trees)
                is Failure -> Failure(actual.value)
            }
        assertEquals(testCase.expected, actualTrees)
    }

    private fun parseTestFile(text: String): TestCase {
        val sections = splitIntoSections(text)
        val inputTokens = parseInputSection(sections.getValue("Input"))

        // a complete key would be "Expected: SUCCESS" or "Expected: FAILURE"
        val expectedKey = getCompleteKey(sections, "Expected")
        val expectedResult = sections.getValue(expectedKey)
        val expected: Either<SyntaxError, List<AST>> =
            when (val status = getStatus(expectedKey)) {
                "SUCCESS" -> Success(parseExpectedTrees(expectedResult))
                "FAILURE" -> Failure(getSyntaxError(expectedResult))
                else -> throw IllegalArgumentException("Status desconocido: $status")
            }

        return TestCase(inputTokens, expected)
    }

    private fun getSyntaxError(expectedResult: List<String>): SyntaxError = SyntaxError.valueOf(expectedResult.first().trim())

    private fun getStatus(expectedKey: String): String = expectedKey.substringAfter(":").trim()

    private fun getCompleteKey(
        sections: Map<String, List<String>>,
        startsWith: String,
    ): String = sections.keys.first { it.startsWith(startsWith) }
}
