package formattest

import formatter.FormatError
import formatter.FormatSuccess
import formatter.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.Reader
import java.io.StringReader
import java.io.Writer

class FormatterStreamingTest {
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

    private class RecordingWriter(
        private val reader: CountingReader,
    ) : Writer() {
        val writes: MutableList<Pair<Int, String>> = mutableListOf()
        private val buffer = StringBuilder()

        override fun write(
            cbuf: CharArray,
            off: Int,
            len: Int,
        ) {
            val chunk = String(cbuf, off, len)
            buffer.append(chunk)
            writes.add(reader.charsRead to chunk)
        }

        override fun flush() = Unit

        override fun close() = Unit

        fun content(): String = buffer.toString()
    }

    @Test
    fun `writes formatted statements incrementally before reading the entire stream`() {
        val stmt1 = "let x:number=1;\n"
        val stmt2 = "println(x);\n"
        val fullSource = stmt1 + stmt2
        val reader = CountingReader(StringReader(fullSource))
        val writer = RecordingWriter(reader)
        val formatter = Formatter.create()
        val config = """{"enforce-spacing-around-equals": true, "enforce-spacing-after-colon-in-declaration": true}"""

        val result = formatter.format(reader, writer, "1.0", config)

        assertInstanceOf(FormatSuccess::class.java, result)
        assertEquals(2, writer.writes.size)
        val (charsReadAtFirstWrite, firstChunk) = writer.writes[0]
        assertEquals("let x: number = 1;${System.lineSeparator()}", firstChunk)
        assertTrue(
            charsReadAtFirstWrite < fullSource.length,
            "Expected first statement to be written before reading full source ($charsReadAtFirstWrite < ${fullSource.length})",
        )
        assertEquals(
            "let x: number = 1;${System.lineSeparator()}println(x);${System.lineSeparator()}",
            writer.content(),
        )
    }

    @Test
    fun `stops on syntax error without reading trailing statements`() {
        val validStmt = "let x:number=1;\n"
        val brokenStmt = "let broken = 2;\n"
        val trailingStmt = "println(x);\nlet y:number=3;\n"
        val fullSource = validStmt + brokenStmt + trailingStmt
        val reader = CountingReader(StringReader(fullSource))
        val writer = RecordingWriter(reader)
        val formatter = Formatter.create()
        val config = """{"enforce-spacing-around-equals": true, "enforce-spacing-after-colon-in-declaration": true}"""

        val result = formatter.format(reader, writer, "1.0", config)

        assertInstanceOf(FormatError::class.java, result)
        assertEquals("let x: number = 1;${System.lineSeparator()}", writer.content())
        assertTrue(
            reader.charsRead < fullSource.length,
            "Expected stream to stop at syntax error, but read ${reader.charsRead} of ${fullSource.length} chars",
        )
    }

    @Test
    fun `invalid version or config fails before reading stream`() {
        val formatter = Formatter.create()

        val reader1 = CountingReader(StringReader("let a:number=1;"))
        val badVersion = formatter.format(reader1, RecordingWriter(reader1), "9.9")
        assertInstanceOf(FormatError::class.java, badVersion)
        assertEquals(0, reader1.charsRead)

        val reader2 = CountingReader(StringReader("let a:number=1;"))
        val badConfig = formatter.format(reader2, RecordingWriter(reader2), "1.0", "{invalid")
        assertInstanceOf(FormatError::class.java, badConfig)
        assertEquals(0, reader2.charsRead)
    }
}
