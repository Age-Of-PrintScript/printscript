package formatter

import domain.Error


enum class FormattingError(val reason: String): Error{
    INVALID_RULES("Formatting rules are invalid"),
    FILE_NOT_FOUND("Rules file could not be read")
}