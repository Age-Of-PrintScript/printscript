package formatter

import domain.Error

enum class ParsingError(val reason: String): Error{
    PARSE_ERROR("Could not parse"),
}