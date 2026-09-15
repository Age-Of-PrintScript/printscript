package parser

import domain.Failure
import domain.NumType
import domain.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.builders.DeclarationParser
import tokens.Assign
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.Token

class TestErrorPositions {
    private val expressionParser = ExpressionParser()
    private val v10Parsers = listOf(DeclarationParser(expressionParser))
    private val parser = Parser.new(v10Parsers)

    @Test
    fun `syntax error captures token position on missing semicolon`() {
        // let x: number = 42 (missing semicolon at end)
        val tokens =
            listOf(
                Token(Let, Position(2, 1), Position(2, 3)),
                Token(Identifier("x"), Position(2, 5), Position(2, 5)),
                Token(Colon, Position(2, 6), Position(2, 6)),
                Token(DataType(NumType), Position(2, 8), Position(2, 13)),
                Token(Assign, Position(2, 15), Position(2, 15)),
                Token(Literal("42", NumType), Position(2, 17), Position(2, 18)),
            )

        val result = parser.parse(tokens)
        assertTrue(result is Failure)
        val error = (result as Failure).value
        assertEquals("Unexpected end of sentence, incomplete statement", error.getMessage())
        assertEquals(Position(2, 18), error.start)
    }

    @Test
    fun `syntax error captures token position on unexpected token`() {
        // let x 42; (missing colon, encounters literal 42 instead)
        val tokens =
            listOf(
                Token(Let, Position(5, 10), Position(5, 12)),
                Token(Identifier("x"), Position(5, 14), Position(5, 14)),
                Token(Literal("42", NumType), Position(5, 16), Position(5, 17)),
            )

        val result = parser.parse(tokens)
        assertTrue(result is Failure)
        val error = (result as Failure).value
        assertEquals("Missing colon in declaration", error.getMessage())
        assertEquals(Position(5, 16), error.start)
        assertEquals(Position(5, 17), error.end)
    }
}
