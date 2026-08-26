package lexer

import lexer.cases.InvalidCharacters
import lexer.cases.MalformedNumbers
import lexer.cases.SuccessfulAssignments
import lexer.cases.SuccessfulCalls
import lexer.cases.SuccessfulDeclarations
import lexer.cases.SuccessfulEdgeCases
import lexer.cases.SuccessfulExpressions
import lexer.cases.UnterminatedStrings
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import tokens.TokenType

data class SuccessCase(
    val name: String,
    val input: String,
    val expected: List<TokenType>,
)

data class FailureCase(
    val name: String,
    val input: String,
    val expected: LexerError,
)

class TestLexer {
    private val lexer = LexerImpl()

    @TestFactory
    fun `successful declarations`(): List<DynamicNode> =
        SuccessfulDeclarations.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectSource(lexer, case.input, case.expected)
            }
        }

    @TestFactory
    fun `successful assignments`(): List<DynamicNode> =
        SuccessfulAssignments.cases().map { case ->
            dynamicTest(case.name) { assertCorrectSource(lexer, case.input, case.expected) }
        }

    @TestFactory
    fun `successful calls`(): List<DynamicNode> =
        SuccessfulCalls.cases().map { case ->
            dynamicTest(case.name) { assertCorrectSource(lexer, case.input, case.expected) }
        }

    @TestFactory
    fun `successful expressions`(): List<DynamicNode> =
        SuccessfulExpressions.cases().map { case ->
            dynamicTest(case.name) { assertCorrectSource(lexer, case.input, case.expected) }
        }

    @TestFactory
    fun `successful edge cases`(): List<DynamicNode> =
        SuccessfulEdgeCases.cases().map { case ->
            dynamicTest(case.name) { assertCorrectSource(lexer, case.input, case.expected) }
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
            dynamicTest(case.name) { assertIncorrectSource(lexer, case.input, case.expected) }
        }

    @TestFactory
    fun `malformed numbers`(): List<DynamicNode> =
        MalformedNumbers.cases().map { case ->
            dynamicTest(case.name) { assertIncorrectSource(lexer, case.input, case.expected) }
        }
}
