package formatter

import domain.Error

enum class FormattingError(
    val reason: String,
) : Error {
    INVALID_JSON("Formatting rules are invalid"),
    UNKNOWN_AST_TYPE("Unknown Ast type of data"),
    ;

    override fun getMessage(): String = reason
}

data class FormatterConfigError(
    private val reason: String,
) : Error {
    override fun getMessage(): String = reason
}
