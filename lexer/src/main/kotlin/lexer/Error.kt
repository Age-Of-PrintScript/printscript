package lexer
import domain.Error

enum class LexerError(
    val mensaje: String,
) : Error {
    INVALID_CHARACTER("Invalid character") {
        override fun getMessage() = mensaje
    },
    INVALID_CHARACTER_FOR_TOKEN_TYPE("Invalid character for current token type") {
        override fun getMessage() = mensaje
    },
    UNTERMINATED_STRING("Unterminated string literal") {
        override fun getMessage() = mensaje
    },
    UNDETERMINED_TOKEN_TYPE("Could not determine token type") {
        override fun getMessage() = mensaje
    },
}
