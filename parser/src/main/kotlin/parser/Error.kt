package parser

import domain.Error

enum class SyntaxError(
    val message: String,
) : Error {
    MISSING_COLON_IN_DECLARATION("Missing colon in declaration"),
    MISSING_ASSIGNMENT_OPERATOR("Expected assignment"),
    MISSING_SEMICOLON("Expected semicolon"),
    MISSING_IDENTIFIER("Missing identifier"),
    MISSING_TYPE_IN_DECLARATION("Missing type in declaration"),
    MISSING_FUNCTION_NAME("Missing function name in call"),
    INVALID_TOKEN_AFTER_TYPE("Expected '=' or ';' after type declaration"),
    INVALID_TOKEN("Unexpected token"),
    INCOMPLETE_STATEMENT("Unexpected end of sentence, incomplete statement"),
    UNEXPECTED_TOKEN_AFTER_STATEMENT("Unexpected token after end of statement, expected semicolon"),
    WRONG_TOKEN_TYPE("Number or variable was expected"),
    MISSING_CLOSING_PARENTHESIS("Missing closing parenthesis of expression"),
    MISSING_FUNCTION_ARGUMENT("Missing function argument in call"),
}
