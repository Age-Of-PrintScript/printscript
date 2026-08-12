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
            is Success -> Success(result.value.parsedResult)
            is Failure -> Failure(result.value)
        }
    }

    private data class ParsedResult<Expression>(val parsedResult: Expression, val nextPosition: Int)


    private fun parseExpressionRecursive(tokens: List<Token>): Either<ParsingError, ParsedResult<Expression>> {
        return when (val term = parseTerm(tokens, 0)) {
            is Success -> parseExpressionRec(tokens, term.value.nextPosition, term.value.parsedResult) // Quedo muy raro el nombre
            is Failure -> Failure(term.value)
        }
    }

    private fun parseTerm(tokens: List<Token>, position: Int): Either<ParsingError, ParsedResult<Expression>> {
        return when (val factor = parseFactor(tokens, position)){
            is Success -> parseTermRec(tokens, factor.value.nextPosition, factor.value.parsedResult)
            is Failure -> Failure(factor.value)
        }
    }

    private fun parseFactor(tokens: List<Token>, position: Int): Either<ParsingError, ParsedResult<Expression>> {
        val token = tokens.getOrNull(position)
            ?: return Failure(SYNTAX_ERROR("token expected, but list ended abruptly")) // Si llegue aca y la lista termino, la expresión no tiene sentido.

        val expression = when (val type = token.type) {
            is Literal -> Expression.Literal(type.value)
            is Identifier -> Expression.Variable(type.name)
            else -> return Failure(SYNTAX_ERROR("number or variable was expected, got this type: $type instead in position $position")) // Si hay un tokenType que no es de los dos de arriba, la expresión no tiene sentido.
        }
        return Success(ParsedResult(expression, position + 1)) // Sigo con mi lista
    }

// métodos auxiliares recursivos

    private tailrec fun parseExpressionRec(tokens: List<Token>, position: Int, left: Expression): Either<ParsingError, ParsedResult<Expression>> {
        val operator = currentOperator(tokens, position, termSeparators)
            ?: return Success(ParsedResult(left, position)) // Si es nulo, no estoy en el medio de una expresión. Me quedo con lo de la izquierda.

        return when (val right = parseTerm(tokens, position + 1)) { //obtengo el otro miembro de la expresión
            is Success -> parseExpressionRec(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parsedResult, operator))
            is Failure -> Failure(right.value)
        }
    }

    private tailrec fun parseTermRec(tokens: List<Token>, position: Int, left: Expression): Either<ParsingError, ParsedResult<Expression>> {
        val operator = currentOperator(tokens, position, factorSeparators)
            ?: return Success(ParsedResult(left, position)) // Si es nulo, no estoy en el medio de un termino. Me quedo con lo de la izquierda.

        return when (val right = parseFactor(tokens, position + 1)){ //obtengo el otro miembro del termino
            is Success -> parseTermRec(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parsedResult, operator))
            is Failure -> Failure(right.value)
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
