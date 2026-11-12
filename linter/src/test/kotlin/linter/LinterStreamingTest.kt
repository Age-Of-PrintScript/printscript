package linter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.Reader
import java.io.StringReader

internal class LinterStreamingTest {
    private class CountingReader(
        private val delegate: Reader,
    ) : Reader() {
        var charsRead: Int = 0
            private set

        override fun read(
            cbuf: CharArray,
            off: Int,
            len: Int,
        ): Int {
            val count = delegate.read(cbuf, off, len)
            if (count > 0) {
                charsRead += count
            }
            return count
        }

        override fun close() = delegate.close()
    }

    @Test
    fun `analyse with reader does not read anything until sequence is consumed`() {
        val source = "println(1 + 2);\nlet validName: number = 10;"
        val reader = CountingReader(StringReader(source))
        val linter = Linter.create()

        linter.analyse(reader, "1.0")

        assertEquals(0, reader.charsRead)
    }

    @Test
    fun `pulling only the first warning stops reading before the rest of the stream`() {
        val firstStatement = "println(1 + 2);\n"
        val restOfProgram =
            "let validName: number = 1;\n" +
                "let anotherValid: string = \"hello\";\n" +
                "println(3 + 4);\n"
        val fullSource = firstStatement + restOfProgram
        val reader = CountingReader(StringReader(fullSource))
        val linter = Linter.create()

        val firstWarning = linter.analyse(reader, "1.0").first()

        assertEquals(1, firstWarning.position.line)
        assertTrue(
            reader.charsRead <= firstStatement.length + 1,
            "Expected lazy pull to stop near first statement (${firstStatement.length}), but read ${reader.charsRead} of ${fullSource.length} chars",
        )
        assertTrue(reader.charsRead < fullSource.length)
    }

    @Test
    fun `pulling warnings one by one advances the reader incrementally`() {
        val stmt1 = "println(1 + 2);\n"
        val stmt2 = "let snake_case: number = 5;\n"
        val fullSource = stmt1 + stmt2
        val reader = CountingReader(StringReader(fullSource))
        val linter = Linter.create()

        val iterator = linter.analyse(reader, "1.0").iterator()

        assertTrue(iterator.hasNext())
        val first = iterator.next()
        val charsAfterFirst = reader.charsRead

        assertEquals(1, first.position.line)
        assertTrue(charsAfterFirst < fullSource.length)

        assertTrue(iterator.hasNext())
        val second = iterator.next()

        assertEquals(2, second.position.line)
        assertTrue(reader.charsRead > charsAfterFirst)
        assertFalse(iterator.hasNext())
    }

    @Test
    fun `streaming stops on syntax error without reading trailing statements`() {
        val beforeError = "let okName: number = 1;\n"
        val errorStatement = "let broken = 2;\n"
        val trailingStatements = "println(1 + 2);\nprintln(3 + 4);\n"
        val fullSource = beforeError + errorStatement + trailingStatements
        val reader = CountingReader(StringReader(fullSource))
        val linter = Linter.create()

        val warnings = linter.analyse(reader, "1.0").toList()

        assertEquals(1, warnings.size)
        assertEquals(2, warnings.first().position.line)
        assertTrue(
            reader.charsRead < fullSource.length,
            "Expected stream to stop at syntax error, but read ${reader.charsRead} of ${fullSource.length} chars",
        )
    }

    @Test
    fun `unsupported version throws immediately before reading stream`() {
        val reader = CountingReader(StringReader("let a: number = 1;"))
        val linter = Linter.create()

        assertThrows(IllegalArgumentException::class.java) {
            linter.analyse(reader, "9.9")
        }
        assertEquals(0, reader.charsRead)
    }
}
