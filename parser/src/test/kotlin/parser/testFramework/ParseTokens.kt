package parser.testFramework

import domain.BoolType
import domain.NumType
import domain.PSOperator
import domain.Position
import domain.StrType
import tokens.Assign
import tokens.Call
import tokens.CloseBraces
import tokens.CloseParen
import tokens.Colon
import tokens.Const
import tokens.DataType
import tokens.Else
import tokens.Identifier
import tokens.If
import tokens.Let
import tokens.Literal
import tokens.OpenBraces
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

private val literalTypeMap =
    mapOf(
        "NUMBER" to NumType,
        "STRING" to StrType,
        "BOOLEAN" to BoolType,
    )

private val operatorMap =
    mapOf(
        "SUM" to TestOperator.SUM,
        "SUBTRACT" to TestOperator.SUBTRACT,
        "MULTIPLY" to TestOperator.MULTIPLY,
        "DIVIDE" to TestOperator.DIVIDE,
    )

private fun parseTokenType(line: String): TokenType {
    val parts = line.split(":", limit = 2).map { it.trim() }
    val tag = parts[0]
    val value = parts.getOrNull(1)

    return when (tag) {
        "LET" -> Let
        "CONST" -> Const
        "IF" -> If
        "ELSE" -> Else
        "OPEN_BRACES" -> OpenBraces
        "CLOSED_BRACES" -> CloseBraces
        "COLON" -> Colon
        "SEMICOLON" -> Semicolon
        "ASSIGN" -> Assign
        "OPEN_PARENTHESIS" -> OpenParen
        "CLOSED_PARENTHESIS" -> CloseParen
        "IDENTIFIER" -> Identifier(value!!)
        "TYPE" -> parseDataType(value!!)
        "LITERAL" -> parseLiteral(value!!)
        "OPERATOR" -> parseOperator(value!!)
        "CALL" -> Call(value!!.lowercase())
        else -> throw IllegalArgumentException("Tag de token desconocido: $tag en línea: $line")
    }
}

private fun parseDataType(name: String): DataType {
    val psType = literalTypeMap[name] ?: throw IllegalArgumentException("Tipo desconocido: $name")
    return DataType(psType)
}

private fun parseLiteral(raw: String): Literal {
    val parts = raw.split(" ", limit = 2)
    if (parts.size == 2) {
        val type = literalTypeMap[parts[0]]
        if (type != null) {
            return Literal(parts[1], type)
        }
    }
    return inferLiteral(raw)
}

private fun inferLiteral(raw: String): Literal =
    when {
        raw == "true" || raw == "false" -> Literal(raw, BoolType)
        raw.toDoubleOrNull() != null -> Literal(raw, NumType)
        else -> Literal(raw, StrType)
    }

private fun parseOperator(name: String): Operator {
    val op = operatorMap[name] ?: throw IllegalArgumentException("Operador desconocido: $name")
    return Operator(op)
}
