package lexer

import lexer.cases.version1_1.InvalidCharacters
import lexer.cases.version1_1.MalformedNumbers
import lexer.cases.version1_1.SuccessfulCalls
import lexer.cases.version1_1.SuccessfulConditionals
import lexer.cases.version1_1.SuccessfulDeclarations
import lexer.cases.version1_1.UnterminatedStrings
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

    @TestFactory
    fun `unterminated strings`(): List<DynamicNode> =
        UnterminatedStrings.cases().map { case ->
            dynamicTest(case.name) {
                assertIncorrectSource(lexer, case.input, case.expected)
            }
        }

    @TestFactory
    fun `invalid characters`(): List<DynamicNode> =
        InvalidCharacters.cases().map { case ->
            dynamicTest(case.name) {
                assertIncorrectSource(lexer, case.input, case.expected)
            }
        }

    @TestFactory
    fun `malformed numbers`(): List<DynamicNode> =
        MalformedNumbers.cases().map { case ->
            dynamicTest(case.name) {
                assertIncorrectSource(lexer, case.input, case.expected)
            }
        }
}
