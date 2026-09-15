package lexer

import domain.NumType
import domain.Position
import domain.StrType
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tokens.Assign
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.Semicolon

class TestTokenPositions {
    private val lexer = LexerImpl(Lexicon(testSymbolsV1_0, testKeywordsV1_0))

    @Test
    fun `token positions are tracked on single line`() {
        val input = "let x: number = 42;"
        val result = lexer.tokenize(input)
        assertTrue(result is Success)

        val tokens = (result as Success).value
        assertEquals(7, tokens.size)

        assertEquals(Let, tokens[0].type)
        assertEquals(Position(1, 1), tokens[0].start)
        assertEquals(Position(1, 3), tokens[0].end)

        assertEquals(Identifier("x"), tokens[1].type)
        assertEquals(Position(1, 5), tokens[1].start)
        assertEquals(Position(1, 5), tokens[1].end)

        assertEquals(Colon, tokens[2].type)
        assertEquals(Position(1, 6), tokens[2].start)
        assertEquals(Position(1, 6), tokens[2].end)

        assertEquals(DataType(NumType), tokens[3].type)
        assertEquals(Position(1, 8), tokens[3].start)
        assertEquals(Position(1, 13), tokens[3].end)

        assertEquals(Assign, tokens[4].type)
        assertEquals(Position(1, 15), tokens[4].start)
        assertEquals(Position(1, 15), tokens[4].end)

        assertEquals(Literal("42", NumType), tokens[5].type)
        assertEquals(Position(1, 17), tokens[5].start)
        assertEquals(Position(1, 18), tokens[5].end)

        assertEquals(Semicolon, tokens[6].type)
        assertEquals(Position(1, 19), tokens[6].start)
        assertEquals(Position(1, 19), tokens[6].end)
    }

    @Test
    fun `token positions are tracked across multiple lines`() {
        val input = "let a = \"hi\";\nlet b = 10;"
        val result = lexer.tokenize(input)
        assertTrue(result is Success)

        val tokens = (result as Success).value
        assertEquals(10, tokens.size)

        // Line 1
        assertEquals(Let, tokens[0].type)
        assertEquals(Position(1, 1), tokens[0].start)
        assertEquals(Position(1, 3), tokens[0].end)

        assertEquals(Literal("hi", StrType), tokens[3].type)
        assertEquals(Position(1, 9), tokens[3].start)
        assertEquals(Position(1, 12), tokens[3].end)

        assertEquals(Semicolon, tokens[4].type)
        assertEquals(Position(1, 13), tokens[4].start)
        assertEquals(Position(1, 13), tokens[4].end)

        // Line 2
        assertEquals(Let, tokens[5].type)
        assertEquals(Position(2, 1), tokens[5].start)
        assertEquals(Position(2, 3), tokens[5].end)

        assertEquals(Identifier("b"), tokens[6].type)
        assertEquals(Position(2, 5), tokens[6].start)
        assertEquals(Position(2, 5), tokens[6].end)

        assertEquals(Assign, tokens[7].type)
        assertEquals(Position(2, 7), tokens[7].start)
        assertEquals(Position(2, 7), tokens[7].end)

        assertEquals(Literal("10", NumType), tokens[8].type)
        assertEquals(Position(2, 9), tokens[8].start)
        assertEquals(Position(2, 10), tokens[8].end)

        assertEquals(Semicolon, tokens[9].type)
        assertEquals(Position(2, 11), tokens[9].start)
        assertEquals(Position(2, 11), tokens[9].end)
    }
}
