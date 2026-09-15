package lexer

import domain.Error
import domain.Position

data class LexerError(
    private val message: String,
    override val start: Position? = null,
    override val end: Position? = null,
) : Error {
    override fun getMessage(): String = message

    fun withPosition(
        start: Position?,
        end: Position? = null,
    ): LexerError = copy(start = start, end = end)

    companion object {
        val INVALID_CHARACTER = LexerError("Invalid character")
        val INVALID_CHARACTER_FOR_TOKEN_TYPE = LexerError("Invalid character for current token type")
        val UNTERMINATED_STRING = LexerError("Unterminated string literal")
        val UNDETERMINED_TOKEN_TYPE = LexerError("Could not determine token type")
    }
}
