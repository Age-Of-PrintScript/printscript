package parser

import ast.AST
import domain.NumType
import domain.Position
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.builders.AssignmentParser
import parser.builders.DeclarationParser
import parser.tokenConsumers.ListTokenConsumer
import parser.tokenConsumers.StreamTokenConsumer
import parser.tokenConsumers.TokenConsumer
import tokens.Assign
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.Semicolon
import tokens.Token

class TestStreamParser {
    private val expressionParser = ExpressionParser()
    private val statementParsers =
        listOf(
            DeclarationParser(expressionParser),
            AssignmentParser(expressionParser),
        )
    private val parser = Parser.new(statementParsers)

    @Test
    fun `parseNext parses statements incrementally from StreamTokenConsumer`() {
        val tokens =
            listOf(
                Token(Let, Position(1, 1), Position(1, 3)),
                Token(Identifier("x"), Position(1, 5), Position(1, 5)),
                Token(Colon, Position(1, 6), Position(1, 6)),
                Token(DataType(NumType), Position(1, 8), Position(1, 13)),
                Token(Assign, Position(1, 15), Position(1, 15)),
                Token(Literal("42", NumType), Position(1, 17), Position(1, 18)),
                Token(Semicolon, Position(1, 19), Position(1, 19)),
                Token(Identifier("x"), Position(2, 1), Position(2, 1)),
                Token(Assign, Position(2, 3), Position(2, 3)),
                Token(Literal("100", NumType), Position(2, 5), Position(2, 7)),
                Token(Semicolon, Position(2, 8), Position(2, 8)),
            )

        var index = 0
        val consumer =
            TokenConsumer.from {
                if (index < tokens.size) tokens[index++] else null
            }

        val s1 = parser.parseNext(consumer)
        assertTrue(s1 is Success && s1.value is AST.DeclarationStatement)
        val decl = (s1 as Success).value as AST.DeclarationStatement
        assertEquals("x", decl.id)

        val s2 = parser.parseNext(consumer)
        assertTrue(s2 is Success && s2.value is AST.AssignmentStatement)
        val assign = (s2 as Success).value as AST.AssignmentStatement
        assertEquals("x", assign.id)

        val s3 = parser.parseNext(consumer)
        assertNull(s3)
    }

    @Test
    fun `token consumer factory methods produce working consumers`() {
        val tokens =
            listOf(
                Token(Identifier("a"), Position(1, 1), Position(1, 1)),
                Token(Identifier("b"), Position(1, 3), Position(1, 3)),
            )

        val fromList = TokenConsumer.from(tokens)
        assertTrue(fromList is ListTokenConsumer)
        assertTrue(fromList.hasNext())
        assertEquals(tokens[0], fromList.peek())
        assertEquals(tokens[0], fromList.consume())
        assertEquals(Position(1, 1), fromList.lastPosition)

        var i = 0
        val fromSupplier = TokenConsumer.from { if (i < tokens.size) tokens[i++] else null }
        assertTrue(fromSupplier is StreamTokenConsumer)
        assertTrue(fromSupplier.hasNext())
        assertEquals(tokens[0], fromSupplier.peek())
        assertEquals(tokens[0], fromSupplier.consume())
        assertEquals(Position(1, 1), fromSupplier.lastPosition)
        assertTrue(fromSupplier.hasNext())
        assertEquals(tokens[1], fromSupplier.consume())
        assertEquals(Position(1, 3), fromSupplier.lastPosition)
        assertFalse(fromSupplier.hasNext())
    }

    @Test
    fun `empty tokens in parse returns empty program`() {
        val result = parser.parse(emptyList())
        assertTrue(result is Success)
        val prog = (result as Success).value
        assertTrue(prog.trees.isEmpty())
    }
}
