package parser

import domain.Error

enum class SyntaxError(message: String): Error {
    MISSING_COLON_IN_DECLARATION("Missing colon in declaration"),
    MISSING_ASSIGNMENT_OPERATOR("Expected assignment"),
    MISSING_SEMICOLON("Expected semicolon"),
    MISSING_IDENTIFIER("Missing identifier"),
    MISSING_FACTOR_IN_EXPRESSION("Number or variable was expected"),
    MISSING_TYPE_IN_DECLARATION("Missing type in declaration"),
    MISSING_FUNCTION_NAME("Missing function name in call"),
    INVALID_TOKEN_AFTER_TYPE("Expected '=' or ';' after type declaration"),
    INVALID_TOKEN("Unexpected token in expression"),
    INCOMPLETE_STATEMENT("Unexpected end of sentence, incomplete statement"),
    UNEXPECTED_TOKEN_AFTER_STATEMENT("Unexpected token after end of statement, expected semicolon")
}