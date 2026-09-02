package formatter

import domain.Error

enum class FormattingError(
    val reason: String,
) : Error {
    INVALID_JSON("Formatting rules are invalid"),
    FILE_NOT_FOUND("Rules file could not be read"),
    UNKNOWN_AST_TYPE("Unknown Ast type of data"),
}
