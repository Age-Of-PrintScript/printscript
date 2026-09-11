package parser.testFramework

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import parser.BlockParser
import parser.ExpressionParser
import parser.Parser
import parser.SyntaxError
import parser.builders.AssignmentParser
import parser.builders.ConditionalParser
import parser.builders.DeclarationParser
import parser.builders.ExpressionStatementParser
import parser.builders.StatementParser
import tokens.Token
import java.io.File
import java.util.stream.Stream
import kotlin.streams.asStream
import kotlin.test.Test
import kotlin.test.assertEquals

internal data class TestCase(
    val version: String,
    val inputTokens: List<Token>,
    val expected: Either<SyntaxError, List<AST>>,
)

internal class ParserFileTests {
    private fun createParserForVersion(version: String): Parser {
        val expressionParser = ExpressionParser()
        val v10Parsers =
            listOf(
                DeclarationParser(expressionParser),
                AssignmentParser(expressionParser),
                ExpressionStatementParser(expressionParser),
            )
        val statementParsers: List<StatementParser> =
            when (version) {
                "1.0" -> v10Parsers
                "1.1" -> v10Parsers + ConditionalParser(BlockParser(v10Parsers), expressionParser)
                else -> throw IllegalArgumentException("Versión no soportada: $version")
            }
        return Parser.new(statementParsers)
    }

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
        runOneTest(File("src/test/resources/parserTests/case_40.md"))
    }

    private fun runOneTest(file: File) {
        val testCase = parseTestFile(file.readText())
        val parser = createParserForVersion(testCase.version)
        val actual = parser.parse(testCase.inputTokens)
        val actualTrees: Either<SyntaxError, List<AST>> =
            when (actual) {
                is Success -> Success(actual.value.trees)
                is Failure -> Failure(actual.value)
            }
        assertEquals(testCase.expected, actualTrees)
    }

    private fun parseTestFile(text: String): TestCase {
        val version = text.lines().first { it.isNotBlank() }.trim()
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

        return TestCase(version, inputTokens, expected)
    }

    private fun getSyntaxError(expectedResult: List<String>): SyntaxError = SyntaxError.valueOf(expectedResult.first().trim())

    private fun getStatus(expectedKey: String): String = expectedKey.substringAfter(":").trim()

    private fun getCompleteKey(
        sections: Map<String, List<String>>,
        startsWith: String,
    ): String = sections.keys.first { it.startsWith(startsWith) }
}
