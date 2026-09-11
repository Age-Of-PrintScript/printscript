package lexer

import lexer.cases.version1_1.SuccessfulCalls
import lexer.cases.version1_1.SuccessfulConditionals
import lexer.cases.version1_1.SuccessfulDeclarations
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class TestLexer11 {
    private val lexer = LexerImpl(Lexicon(testSymbolsV1_1, testKeywordsV1_1))

    @TestFactory
    fun `successful declarations`(): List<DynamicNode> =
        SuccessfulDeclarations.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectSource(lexer, case.input, case.expected)
            }
        }

    @TestFactory
    fun `successful conditionals`(): List<DynamicNode> =
        SuccessfulConditionals.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectSource(lexer, case.input, case.expected)
            }
        }

    @TestFactory
    fun `successful calls`(): List<DynamicNode> =
        SuccessfulCalls.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectSource(lexer, case.input, case.expected)
            }
        }
}
