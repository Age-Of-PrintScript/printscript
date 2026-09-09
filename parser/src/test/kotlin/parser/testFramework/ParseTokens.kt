package parser.testFramework

import domain.NumType
import domain.PSOperator
import domain.Position
import domain.StrType
import tokens.Assign
import tokens.Call
import tokens.CloseParen
import tokens.Colon
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Literal
import tokens.OpenParen
import tokens.Operator
import tokens.Semicolon
import tokens.Token
import tokens.TokenType

private val dummyPosition = Position(0, 0)

internal enum class TestOperator(
    override val symbol: String,
    override val precedence: Int,
) : PSOperator {
    SUM("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
}

internal fun parseInputSection(lines: List<String>): List<Token> =
    lines.map { line ->
        Token(parseTokenType(line), dummyPosition, dummyPosition)
    }

private fun parseTokenType(line: String): TokenType {
    val parts = line.split(":", limit = 2).map { it.trim() }
    val tag = parts[0]
    val value = parts.getOrNull(1)

    return when (tag) {
        "LET" -> Let
        "COLON" -> Colon
        "SEMICOLON" -> Semicolon
        "ASSIGN" -> Assign
        "OPEN_PARENTHESIS" -> OpenParen
        "CLOSED_PARENTHESIS" -> CloseParen
        "IDENTIFIER" -> Identifier(value!!)
        "TYPE" ->
            when (value) {
                "NUMBER" -> DataType(NumType)
                "STRING" -> DataType(StrType)
                else -> throw IllegalArgumentException("Tipo desconocido: $value")
            }
        "LITERAL" -> {
            val literalVal = value!!
            if (literalVal.toDoubleOrNull() != null) {
                Literal(literalVal, NumType)
            } else {
                Literal(literalVal, StrType)
            }
        }
        "OPERATOR" ->
            when (value) {
                "SUM" -> Operator(TestOperator.SUM)
                "SUBTRACT" -> Operator(TestOperator.SUBTRACT)
                "MULTIPLY" -> Operator(TestOperator.MULTIPLY)
                "DIVIDE" -> Operator(TestOperator.DIVIDE)
                else -> throw IllegalArgumentException("Operador desconocido: $value")
            }
        "CALL" -> Call(value!!.lowercase())
        else -> throw IllegalArgumentException("Tag de token desconocido: $tag en línea: $line")
    }
}
