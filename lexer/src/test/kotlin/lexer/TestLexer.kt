package lexer

import domain.Failure
import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import lexer.cases.SuccessfulAssignments
import lexer.cases.SuccessfulCalls
import lexer.cases.SuccessfulDeclarations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import tokens.ASSIGN
import tokens.COLON
import tokens.Call
import tokens.ClosedParenthesis
import tokens.DataType
import tokens.Identifier
import tokens.LET
import tokens.Literal
import tokens.OpenParenthesis
import tokens.Operator
import tokens.SEMICOLON
import tokens.Token
import tokens.TokenType

data class SuccessCase(
    val name: String,
    val input: String,
    val expected: List<TokenType>
)

data class FailureCase(
    val name: String,
    val input: String,
    val expected: LexerError
)

class TestLexer {
    private val lexer = LexerImpl()

    @TestFactory
    fun `successful declarations`(): List<DynamicNode> =
        SuccessfulDeclarations.cases().map { case ->
            dynamicTest(case.name) { assertCorrectSource(lexer, case.input, case.expected) }
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

}