package parser

import ast.Expression
import domain.Either
import domain.Failure
import domain.PSOperator
import domain.Position
import domain.Success
import domain.getOrReturn
import parser.tokenConsumers.TokenConsumer
import tokens.Call
import tokens.CloseParen
import tokens.Identifier
import tokens.Literal
import tokens.OpenParen
import tokens.Operator
import tokens.Token

class ExpressionParser {
    fun parse(tokens: List<Token>): Either<SyntaxError, Expression> {
        if (tokens.isEmpty()) return Failure(SyntaxError.INCOMPLETE_STATEMENT)
        val consumer = TokenConsumer(tokens)
        val result = parseExpression(consumer, 0).getOrReturn { return Failure(it) }
        if (consumer.hasNext()) {
            val token = consumer.peek()
            return Failure(SyntaxError.INVALID_TOKEN.withPosition(token.start, token.end))
        }
        return Success(result)
    }

    // Precedence Climbing: parsea el lado izquierdo y luego
    // va enganchando operadores mientras su precedencia sea >= a la mínima pedida
    private fun parseExpression(
        consumer: TokenConsumer,
        minPrecedence: Int,
    ): Either<SyntaxError, Expression> {
        var left = parsePrimary(consumer).getOrReturn { return Failure(it) }

        while (true) {
            val op = nextOperator(consumer, minPrecedence) ?: break

            consumer.consume()
            val right = parseExpression(consumer, op.precedence + 1).getOrReturn { return Failure(it) }
            left = Expression.Operation(left, op, right)
        }

        return Success(left)
    }

    // Parsea un valor atómico: literal, variable, llamada a función, o expresión entre paréntesis
    private fun parsePrimary(consumer: TokenConsumer): Either<SyntaxError, Expression> {
        if (!consumer.hasNext()) {
            val pos = lastTokenEnd(consumer)
            return Failure(SyntaxError.INCOMPLETE_STATEMENT.withPosition(pos, pos))
        }

        val token = consumer.consume()
        return when (val type = token.type) {
            is Literal -> Success(Expression.Literal(type.value, type.type))
            is Identifier -> Success(Expression.Variable(type.name))
            is Call -> parseCall(type.name, consumer)
            is OpenParen -> parseGrouped(consumer)
            else -> Failure(SyntaxError.WRONG_TOKEN_TYPE.withPosition(token.start, token.end))
        }
    }

    // Parsea los argumentos de una llamada: nombre( expresión )
    private fun parseCall(
        name: String,
        consumer: TokenConsumer,
    ): Either<SyntaxError, Expression> {
        if (!consumer.hasNext()) {
            val pos = lastTokenEnd(consumer)
            return Failure(SyntaxError.INCOMPLETE_STATEMENT.withPosition(pos, pos))
        }
        val peekToken = consumer.peek()
        if (peekToken.type !is OpenParen) {
            return Failure(SyntaxError.INVALID_TOKEN.withPosition(peekToken.start, peekToken.end))
        }
        consumer.consume() // Consume '('

        if (consumer.hasNext() && consumer.peek().type is CloseParen) {
            consumer.consume() // Consume ')'
            return Success(Expression.Call(name, emptyList()))
        }

        val arg = parseExpression(consumer, 0).getOrReturn { return Failure(it) }
        if (!consumer.hasNext() || consumer.peek().type !is CloseParen) {
            val (errStart, errEnd) =
                if (consumer.hasNext()) {
                    val nextToken = consumer.peek()
                    nextToken.start to nextToken.end
                } else {
                    val pos = lastTokenEnd(consumer)
                    pos to pos
                }
            return Failure(SyntaxError.MISSING_CLOSING_PARENTHESIS.withPosition(errStart, errEnd))
        }
        consumer.consume() // Consume ')'
        return Success(Expression.Call(name, listOf(arg)))
    }

    // Parsea una expresión entre paréntesis: ( expresión )
    private fun parseGrouped(consumer: TokenConsumer): Either<SyntaxError, Expression> {
        val inner = parseExpression(consumer, 0).getOrReturn { return Failure(it) }
        if (!consumer.hasNext() || consumer.peek().type !is CloseParen) {
            val (errStart, errEnd) =
                if (consumer.hasNext()) {
                    val nextToken = consumer.peek()
                    nextToken.start to nextToken.end
                } else {
                    val pos = lastTokenEnd(consumer)
                    pos to pos
                }
            return Failure(SyntaxError.MISSING_CLOSING_PARENTHESIS.withPosition(errStart, errEnd))
        }
        consumer.consume() // Consume ')'
        return Success(inner)
    }

    private fun lastTokenEnd(consumer: TokenConsumer): Position = consumer.lastPosition ?: Position.START

    // Devuelve el operador si el siguiente token es un operador con precedencia suficiente, o null si hay que parar
    private fun nextOperator(
        consumer: TokenConsumer,
        minPrecedence: Int,
    ): PSOperator? {
        if (!consumer.hasNext()) return null
        val op = (consumer.peek().type as? Operator)?.operator ?: return null
        if (op.precedence < minPrecedence) return null
        return op
    }
}
