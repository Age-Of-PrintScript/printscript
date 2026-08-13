package lexer

import domain.Failure
import domain.Position
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import jdk.internal.vm.vector.VectorSupport.test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tokens.ASSIGN
import tokens.COLON
import tokens.DataType
import tokens.Identifier
import tokens.LET
import tokens.Literal
import tokens.SEMICOLON
import tokens.Token
import tokens.TokenType


class TestLexer {
    private val lexer = LexerImpl()
    @Test
    fun `test-normal-declaration`(){
        val input = "let x: number = 5;"
        val expected = listOf(
            LET,
            Identifier("x"),
            COLON,
            DataType(PrintScriptType.NUMBER),
            ASSIGN,
            Literal(PrintScriptValue.NumberLiteral(5)),
            SEMICOLON)
        assertCorrectSource(input, createTokens(expected))
    }



    private fun assertCorrectSource(input: String, expected: List<Token>){
        val result = lexer.tokenize(input)
        assertTrue(result is Success, "Tokenization wasn't successful ${(result as Failure).value}")
        val givenTokens = (result as Success<LexerError, List<Token>>).value
        assertEqualTokenList(expected, givenTokens)
    }
    private fun assertEqualTokenList(expected: List<Token>, actual: List<Token>){
        if (expected.size != actual.size) error("Expected ${expected.size} tokenlist size || Actual ${actual.size} token list size")
        for(i in expected.indices){
            assertEquals(expected[i], actual[i], "Expected: ${expected[i]} || Actual: ${actual[i]}")
        }
    }
    private fun assertIncorrectSource(input: String, expected: LexerError){
        val result = lexer.tokenize(input)
        assertTrue(result is Failure)
        assertEquals(expected, (result as Failure<LexerError, List<Token>>).value)
    }
    private fun createTokens(types: List<TokenType>): List<Token> {
        return types.map{
            Token(
                it,
                Position(0,0),
                Position(0,0)
            )
        }
    }
}
