package parser.testFramework

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue
import tokens.ASSIGN
import tokens.COLON
import tokens.Call
import tokens.CLOSED_PARENTHESIS
import tokens.DataType
import tokens.Identifier
import tokens.LET
import tokens.Literal
import tokens.OPEN_PARENTHESIS
import tokens.Operator
import tokens.SEMICOLON
import tokens.Token
import tokens.TokenType

private val dummyPosition = Position(0, 0)

internal fun parseInputSection(lines: List<String>): List<Token> {
    return lines.map { line ->
        Token(parseTokenType(line), dummyPosition, dummyPosition)
    }
}

private fun parseTokenType(line: String): TokenType {
    val parts = line.split(":", limit = 2).map { it.trim() }
    val tag = parts[0]
    val value = parts.getOrNull(1)

    return when (tag) {
        "LET" -> LET
        "COLON" -> COLON
        "SEMICOLON" -> SEMICOLON
        "ASSIGN" -> ASSIGN
        "OPEN_PARENTHESIS" -> OPEN_PARENTHESIS
        "CLOSED_PARENTHESIS" -> CLOSED_PARENTHESIS
        "IDENTIFIER" -> Identifier(value!!)
        "TYPE" -> DataType(PrintScriptType.valueOf(value!!))
        "LITERAL" -> Literal(value!!)
        "OPERATOR" -> Operator(PrintScriptOperator.valueOf(value!!))
        "CALL" -> Call(PrintScriptFunctions.valueOf(value!!))
        else -> throw IllegalArgumentException("Tag de token desconocido: $tag en línea: $line")
    }
}