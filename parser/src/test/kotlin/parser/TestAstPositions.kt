package parser

import ast.AST
import domain.NumType
import domain.Position
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.builders.AssignmentParser
import parser.builders.ConditionalParser
import parser.builders.DeclarationParser
import parser.builders.ExpressionStatementParser
import tokens.Assign
import tokens.CloseBraces
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.If
import tokens.Let
import tokens.Literal
import tokens.OpenBraces
import tokens.Semicolon
import tokens.Token

class TestAstPositions {
    private val expressionParser = ExpressionParser()

    private val v10Parsers =
        listOf(
            DeclarationParser(expressionParser),
            AssignmentParser(expressionParser),
            ExpressionStatementParser(expressionParser),
        )

    private val v11Parsers =
        run {
            val parsers = mutableListOf<parser.builders.StatementParser>()
            val blockParser = BlockParser(parsers)
            val conditionalParser = ConditionalParser(blockParser, expressionParser)
            parsers.addAll(v10Parsers)
            parsers.add(conditionalParser)
            parsers
        }

    @Test
    fun `declaration statement captures start and end position`() {
        val parser = Parser.new(v10Parsers)
        // let x: number = 42;
        val tokens =
            listOf(
                Token(Let, Position(1, 1), Position(1, 3)),
                Token(Identifier("x"), Position(1, 5), Position(1, 5)),
                Token(Colon, Position(1, 6), Position(1, 6)),
                Token(DataType(NumType), Position(1, 8), Position(1, 13)),
                Token(Assign, Position(1, 15), Position(1, 15)),
                Token(Literal("42", NumType), Position(1, 17), Position(1, 18)),
                Token(Semicolon, Position(1, 19), Position(1, 19)),
            )

        val result = parser.parse(tokens)
        assertTrue(result is Success)

        val program = (result as Success).value
        assertEquals(Position(1, 1), program.start)
        assertEquals(Position(1, 19), program.end)

        val statement = program.trees.first() as AST.DeclarationStatement
        assertEquals(Position(1, 1), statement.start)
        assertEquals(Position(1, 19), statement.end)
    }

    @Test
    fun `assignment statement captures start and end position`() {
        val parser = Parser.new(v10Parsers)
        // x = 10;
        val tokens =
            listOf(
                Token(Identifier("x"), Position(2, 5), Position(2, 5)),
                Token(Assign, Position(2, 7), Position(2, 7)),
                Token(Literal("10", NumType), Position(2, 9), Position(2, 10)),
                Token(Semicolon, Position(2, 11), Position(2, 11)),
            )

        val result = parser.parse(tokens)
        assertTrue(result is Success)

        val program = (result as Success).value
        val statement = program.trees.first() as AST.AssignmentStatement
        assertEquals(Position(2, 5), statement.start)
        assertEquals(Position(2, 11), statement.end)
    }

    @Test
    fun `conditional statement captures start and closing brace end position`() {
        val parser = Parser.new(v11Parsers)
        // if (true) { x = 1; }
        val tokens =
            listOf(
                Token(If, Position(3, 1), Position(3, 2)),
                Token(Identifier("true"), Position(3, 4), Position(3, 7)),
                Token(OpenBraces, Position(3, 9), Position(3, 9)),
                Token(Identifier("x"), Position(3, 11), Position(3, 11)),
                Token(Assign, Position(3, 13), Position(3, 13)),
                Token(Literal("1", NumType), Position(3, 15), Position(3, 15)),
                Token(Semicolon, Position(3, 16), Position(3, 16)),
                Token(CloseBraces, Position(3, 18), Position(3, 18)),
            )

        val result = parser.parse(tokens)
        assertTrue(result is Success)

        val program = (result as Success).value
        val statement = program.trees.first() as AST.ConditionalStatement
        assertEquals(Position(3, 1), statement.start)
        assertEquals(Position(3, 18), statement.end)
    }
}
