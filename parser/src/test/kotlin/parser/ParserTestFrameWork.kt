package parser

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File
import java.util.stream.Stream
import kotlin.streams.asStream
import kotlin.test.assertEquals

class ParserFileTests {
    @TestFactory
    fun runAllParserTests(): Stream<DynamicTest> {
        val dir = File("src/test/resources/parserTests")
        return dir.listFiles()!!.asSequence()
            .map { file ->
                DynamicTest.dynamicTest(file.name) {
                    runOneTest(file)
                }
            }
            .asStream()
    }

    private fun runOneTest(file: File) {
        val testCase = parseTestFile(file.readText())   // step 2, you write this
        val actual = ParserImpl().parse(testCase.inputTokens)
        assertEquals(testCase.expected, actual)
    }

    private fun parseTestFile(readText: String) {
        TODO("Not yet implemented")
    }
}
