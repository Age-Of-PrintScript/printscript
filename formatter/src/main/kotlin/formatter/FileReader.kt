package formatter

import kotlinx.serialization.json.JsonNull.content

interface FileReader {
    fun readText(path: String): String
}

class FileReaderFake(
    content: String,
) : FileReader {
    override fun readText(path: String): String = content
}
