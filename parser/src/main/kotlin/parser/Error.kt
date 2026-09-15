package parser

import domain.Error
import domain.Position

data class SyntaxError(
    private val message: String,
    override val start: Position? = null,
    override val end: Position? = null,
) : Error {
    override fun getMessage(): String = message

    fun withPosition(
        start: Position?,
        end: Position? = null,
    ): SyntaxError = copy(start = start, end = end)

    companion object {
        val MISSING_COLON_IN_DECLARATION = SyntaxError("Missing colon in declaration")
        val MISSING_ASSIGNMENT_OPERATOR = SyntaxError("Expected assignment")
        val MISSING_SEMICOLON = SyntaxError("Expected semicolon")
        val MISSING_IDENTIFIER = SyntaxError("Missing identifier")
        val MISSING_TYPE_IN_DECLARATION = SyntaxError("Missing type in declaration")
        val MISSING_FUNCTION_NAME = SyntaxError("Missing function name in call")
        val INVALID_TOKEN_AFTER_TYPE = SyntaxError("Expected '=' or ';' after type declaration")
        val INVALID_TOKEN = SyntaxError("Unexpected token")
        val INCOMPLETE_STATEMENT = SyntaxError("Unexpected end of sentence, incomplete statement")
        val UNEXPECTED_TOKEN_AFTER_STATEMENT =
            SyntaxError(
                "Unexpected token after end of statement, expected semicolon",
            )
        val WRONG_TOKEN_TYPE = SyntaxError("Number or variable was expected")
        val MISSING_CLOSING_PARENTHESIS = SyntaxError("Missing closing parenthesis of expression")
        val MISSING_FUNCTION_ARGUMENT = SyntaxError("Missing function argument in call")

        fun valueOf(name: String): SyntaxError =
            when (name) {
                "MISSING_COLON_IN_DECLARATION" -> MISSING_COLON_IN_DECLARATION
                "MISSING_ASSIGNMENT_OPERATOR" -> MISSING_ASSIGNMENT_OPERATOR
                "MISSING_SEMICOLON" -> MISSING_SEMICOLON
                "MISSING_IDENTIFIER" -> MISSING_IDENTIFIER
                "MISSING_TYPE_IN_DECLARATION" -> MISSING_TYPE_IN_DECLARATION
                "MISSING_FUNCTION_NAME" -> MISSING_FUNCTION_NAME
                "INVALID_TOKEN_AFTER_TYPE" -> INVALID_TOKEN_AFTER_TYPE
                "INVALID_TOKEN" -> INVALID_TOKEN
                "INCOMPLETE_STATEMENT" -> INCOMPLETE_STATEMENT
                "UNEXPECTED_TOKEN_AFTER_STATEMENT" -> UNEXPECTED_TOKEN_AFTER_STATEMENT
                "WRONG_TOKEN_TYPE" -> WRONG_TOKEN_TYPE
                "MISSING_CLOSING_PARENTHESIS" -> MISSING_CLOSING_PARENTHESIS
                "MISSING_FUNCTION_ARGUMENT" -> MISSING_FUNCTION_ARGUMENT
                else -> throw IllegalArgumentException("Unknown SyntaxError: $name")
            }
    }
}
