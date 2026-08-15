package lexer

import domain.Failure
import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
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


class TestLexer {
    private val lexer = LexerImpl()
    @Test
    fun `test normal declaration`(){
        val input = "let x: number = 5;"
        val expected = listOf(
            LET,
            Identifier("x"),
            COLON,
            DataType(PrintScriptType.NUMBER),
            ASSIGN,
            Literal(PrintScriptValue.NumberLiteral(5)),
            SEMICOLON)
        assertCorrectSource(lexer, input, expected)
    }
    @Test
    fun `test declaration with no assignment`(){
        val input = "let x: number;"
        val expected = listOf(
            LET,
            Identifier("x"),
            COLON,
            DataType(PrintScriptType.NUMBER),
            SEMICOLON)
        assertCorrectSource(lexer, input, expected)
    }
    @Test
    fun `test assignment with expression`(){
        val input = "x = 5 + 2;"
        val expected = listOf(
            Identifier("x"),
            ASSIGN,
            Literal(PrintScriptValue.NumberLiteral(5)),
            Operator(PrintScriptOperator.SUM),
            Literal(PrintScriptValue.NumberLiteral(2)),
            SEMICOLON)
        assertCorrectSource(lexer, input, expected)
    }
    @Test
    fun `test-normal-assignment`(){
        val input = "x = 5;"
        val expected = listOf(
            Identifier("x"),
            ASSIGN,
            Literal(PrintScriptValue.NumberLiteral(5)),
            SEMICOLON)
        assertCorrectSource(lexer,input, expected)
    }
    @Test
    fun `test-normal-call`(){
        val input = "println(5);"
        val expected = listOf(
            Call(PrintScriptFunctions.PRINTLN),
            OpenParenthesis,
            Literal(PrintScriptValue.NumberLiteral(5)),
            ClosedParenthesis,
            SEMICOLON)
        assertCorrectSource(lexer,input, expected)
    }
}
