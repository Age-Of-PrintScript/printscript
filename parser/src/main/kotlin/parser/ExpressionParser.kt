package parser

import domain.Either
import domain.PrintScriptOperator
import domain.Success
import ast.Expression
import domain.Failure
import domain.factorSeparators
import domain.termSeparators
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.Token

internal class ExpressionParser {

    fun parseExpression(expression: List<Token>): Either<ParsingError, Expression> {
        return when (val result = parseExpressionRecursive(expression)) {
            is Success -> Success(result.value.parseResult)
            is Failure<*, *> -> Failure(SYNTAX_ERROR("unexpected error"))
        }
    }

    private data class ParseResult<T>(val parseResult: T, val nextPosition: Int)


    private fun parseExpressionRecursive(tokens: List<Token>): Either<ParsingError, ParseResult<Expression>> {
        return when (val term = parseTerm(tokens, 0)) {
            is Success -> parseExpressionRec(tokens, term.value.nextPosition, term.value.parseResult) //quedo muy raro el nombre
            is Failure<*, *> -> Failure(SYNTAX_ERROR("unexpected error"))
        }
    }

    private fun parseTerm(tokens: List<Token>, position: Int): Either<ParsingError, ParseResult<Expression>> {
        return when (val factor = parseFactor(tokens, position)){
            is Success -> parseTermRec(tokens, factor.value.nextPosition, factor.value.parseResult)
            is Failure<*, *> -> Failure(SYNTAX_ERROR("unexpected error"))
        }
    }

    private fun parseFactor(tokens: List<Token>, position: Int): Either<ParsingError, ParseResult<Expression>> {
        val token = tokens.getOrNull(position)
            ?: return Failure(NO_ASSIGNMENT_ERROR("token expected, but list ended abruptly"))

        val expression = when (val type = token.type) {
            is Literal -> Expression.Literal(type.value)
            is Identifier -> Expression.Variable(type.name)
            else -> return Failure(SYNTAX_ERROR("number or variable was expected, got this type: $type instead in position $position"))
        }
        return Success(ParseResult(expression, position + 1))
    }

// métodos auxiliares recursivos

    private tailrec fun parseExpressionRec(tokens: List<Token>, position: Int, left: Expression): Either<ParsingError, ParseResult<Expression>> {
        val operator = currentOperator(tokens, position, termSeparators)
            ?: return Success(ParseResult(left, position)) // Si es nulo, entonces no hay una expression. Me quedo con lo de la izquierda.

        return when (val right = parseTerm(tokens, position + 1)) {
            is Success -> parseExpressionRec(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parseResult, operator))
            is Failure<*, *> -> Failure(SYNTAX_ERROR("unexpected error"))
        }
    }

    private tailrec fun parseTermRec(tokens: List<Token>, position: Int, left: Expression): Either<ParsingError, ParseResult<Expression>> {
        val operator = currentOperator(tokens, position, factorSeparators)
            ?: return Success(ParseResult(left, position))

        return when (val right = parseFactor(tokens, position + 1)){
            is Success -> parseTermRec(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parseResult, operator))
            is Failure<*, *> -> Failure(SYNTAX_ERROR("unexpected error"))
        }
    }



    /** Devuelve el operador que está en esa posición de la lista de tokens,
     * PERO solo si ese operador es uno de los que le pasaste como argumento (SUM, SUBTRACT, etc.)
     * Si el token en esa posición no es un operador, o es un operador que no está en la lista que le pasaste, o la posición está fuera de rango — devuelve null en cualquiera de esos casos. (muy feo)
     **/


    private fun currentOperator(tokens: List<Token>, position: Int, operators: List<PrintScriptOperator>): PrintScriptOperator? {
        val type = tokens.getOrNull(position)?.type
        return if (type is Operator && type.operator in operators) type.operator else null
    }

}
