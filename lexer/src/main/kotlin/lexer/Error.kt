package lexer
import domain.Error

enum class LexerError(val mensaje: String): Error {
    INVALID_CHARACTER("Invalid character"),
    INVALID_CHARACTER_FOR_TOKEN_TYPE("Invalid character for current token type"),
    UNTERMINATED_STRING("Unterminated string literal"),
    UNDETERMINED_TOKEN_TYPE("Could not determine token type"),
}
