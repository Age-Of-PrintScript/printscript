package lexer

import domain.Failure
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tokens.Call
import tokens.CloseParen
import tokens.Let
import tokens.Literal
import tokens.OpenParen
import tokens.Semicolon
import tokens.Token
import java.io.Reader
import java.io.StringReader

class TestStreamLexer {
    private val lexicon = Lexicon(testSymbolsV1_0, testKeywordsV1_0)
    private val lexer = Lexer.new(lexicon)

    @Test
    fun `nextToken reads tokens one by one and returns null at EOF`() {
        val code = "let x = 5;"
        val reader = StringReader(code)

        val t1 = lexer.nextToken(reader)
        assertTrue(t1 is Success && t1.value.type is Let)

        val t2 = lexer.nextToken(reader)
        assertTrue(t2 is Success && (t2.value.type as tokens.Identifier).name == "x")

        val t3 = lexer.nextToken(reader)
        assertTrue(t3 is Success && t3.value.type is tokens.Assign)

        val t4 = lexer.nextToken(reader)
        assertTrue(t4 is Success && (t4.value.type as Literal).value == "5")

        val t5 = lexer.nextToken(reader)
        assertTrue(t5 is Success && t5.value.type is Semicolon)

        val t6 = lexer.nextToken(reader)
        assertNull(t6)
    }

    @Test
    fun `nextToken accumulates into complete TokenList`() {
        val code = "let x = 5;"
        val reader = StringReader(code)
        val tokens = mutableListOf<Token>()
        while (true) {
            val result = lexer.nextToken(reader) ?: break
            assertTrue(result is Success, "Unexpected error: $result")
            tokens.add((result as Success).value)
        }
        assertEquals(5, tokens.size)
    }

    @Test
    fun `nextToken handles empty and whitespace-only streams`() {
        assertNull(lexer.nextToken(StringReader("")))
        assertNull(lexer.nextToken(StringReader("   \n\t  \r\n ")))
    }

    @Test
    fun `nextToken returns failure on invalid character and unterminated string`() {
        val invalidReader = StringReader("@")
        val res = lexer.nextToken(invalidReader)
        assertTrue(res is Failure && res.value == LexerError.INVALID_CHARACTER)

        val unterminatedReader = StringReader("\"hello")
        val res2 = lexer.nextToken(unterminatedReader)
        assertTrue(res2 is Failure && res2.value == LexerError.UNTERMINATED_STRING)
    }

    @Test
    fun `nextToken streams 100_000 lines without memory accumulation`() {
        val line = "println(\"test\");\n"
        val totalLines = 100_000
        var currentLine = 0
        var charIndex = 0

        val lazyReader =
            object : Reader() {
                override fun read(
                    cbuf: CharArray,
                    off: Int,
                    len: Int,
                ): Int = throw UnsupportedOperationException()

                override fun read(): Int {
                    if (charIndex >= line.length) {
                        charIndex = 0
                        currentLine++
                    }
                    return if (currentLine < totalLines) {
                        val char = line[charIndex]
                        charIndex++
                        char.code
                    } else {
                        -1
                    }
                }

                override fun close() = Unit
            }

        var statementCount = 0
        while (true) {
            val t1 = lexer.nextToken(lazyReader) ?: break
            val t2 = lexer.nextToken(lazyReader)
            val t3 = lexer.nextToken(lazyReader)
            val t4 = lexer.nextToken(lazyReader)
            val t5 = lexer.nextToken(lazyReader)

            assertTrue(t1 is Success && t1.value.type is Call)
            assertTrue(t2 is Success && t2.value.type is OpenParen)
            assertTrue(t3 is Success && t3.value.type is Literal)
            assertTrue(t4 is Success && t4.value.type is CloseParen)
            assertTrue(t5 is Success && t5.value.type is Semicolon)

            statementCount++
        }

        assertEquals(totalLines, statementCount)
    }
}
