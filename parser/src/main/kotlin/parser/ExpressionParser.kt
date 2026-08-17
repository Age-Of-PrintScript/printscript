package parser

import domain.Either
import domain.PrintScriptOperator
import domain.Success
import ast.Expression
import domain.Failure
import domain.factorSeparators
import domain.termSeparators
import tokens.ClosedParenthesis
import tokens.Identifier
import tokens.Literal
import tokens.OpenParenthesis
import tokens.Operator
import tokens.Token
import java.util.Optional

internal class ExpressionParser {

    fun parseExpression(expression: List<Token>): Either<SyntaxError, Expression> {
        return when (val result = createExpressionTree(expression)) {
            is Success -> Success(result.value.parsedResult)
            is Failure -> Failure(result.value)
        }
    }

    private data class ParsedResult<Expression>(val parsedResult: Expression, val nextPosition: Int)


    private fun createExpressionTree(tokens: List<Token>): Either<SyntaxError, ParsedResult<Expression>> {
        return when (val term = parseTerm(tokens, 0)) {
            is Success -> separateExpression(tokens, term.value.nextPosition, term.value.parsedResult) // Quedo muy raro el nombre
            is Failure -> Failure(term.value)
        }
    }

    private fun parseTerm(tokens: List<Token>, position: Int): Either<SyntaxError, ParsedResult<Expression>> {
        return when (val factor = parseFactor(tokens, position)){
            is Success -> separateTerm(tokens, factor.value.nextPosition, factor.value.parsedResult)
            is Failure -> Failure(factor.value)
        }
    }

    private fun parseFactor(
        tokens: List<Token>,
        position: Int
    ): Either<SyntaxError, ParsedResult<Expression>> {
        val token = tokens.getOrNull(position)
            ?: return Failure(SyntaxError.INCOMPLETE_STATEMENT) // Si llegue aca y la lista termino, la expresión no tiene sentido.

        return when (val type = token.type) {
            is Literal -> Success(ParsedResult(Expression.Literal(type.value), position + 1))
            is Identifier -> Success(ParsedResult(Expression.Variable(type.name), position + 1))
            is OpenParenthesis -> parseParenthesisExpression(tokens, position + 1)
            else -> Failure(SyntaxError.WRONG_TOKEN_TYPE) // Si hay un tokenType que no es de los dos de arriba, la expresión no tiene sentido.
        }
    }

    private fun parseParenthesisExpression(tokens: List<Token>,
                                           position: Int):
            Either<SyntaxError, ParsedResult<Expression>> {
        return when (val expression = createExpressionTree(tokens.subList(position, tokens.size))) {
            is Success -> checkClosingParenthesis(tokens, position + expression.value.nextPosition, expression.value)
            is Failure -> expression
        }
    }

    private fun checkClosingParenthesis(
        tokens: List<Token>,
        position: Int,
        parsedExpression: ParsedResult<Expression>
    ): Either<SyntaxError, ParsedResult<Expression>> {
        return when (tokens.getOrNull(position)?.type) {
            is ClosedParenthesis -> Success(ParsedResult(parsedExpression.parsedResult, position + 1))
            else -> Failure(SyntaxError.MISSING_CLOSING_PARENTHESIS)
        }
    }

// métodos auxiliares recursivos

    private tailrec fun separateExpression(tokens: List<Token>, position: Int, left: Expression): Either<SyntaxError, ParsedResult<Expression>> {
        val operator = currentOperator(tokens, position, termSeparators)
            if(operator.isEmpty) return Success(ParsedResult(left, position)) // Si es empty, no estoy en el medio de una expresión. Me quedo con lo de la izquierda.

        return when (val right = parseTerm(tokens, position + 1)) { //obtengo el otro miembro de la expresión
            is Success -> separateExpression(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parsedResult, operator.get()))
            is Failure -> Failure(right.value)
        }
    }

    private tailrec fun separateTerm(tokens: List<Token>, position: Int, left: Expression): Either<SyntaxError, ParsedResult<Expression>> {
        val operator = currentOperator(tokens, position, factorSeparators)
            if(operator.isEmpty) return Success(ParsedResult(left, position)) // Si es empty, no estoy en el medio de un termino. Me quedo con lo de la izquierda.

        return when (val right = parseFactor(tokens, position + 1)){ //obtengo el otro miembro del término
            is Success -> separateTerm(tokens, right.value.nextPosition, Expression.Operation(left, right.value.parsedResult, operator.get()))
            is Failure -> Failure(right.value)
        }
    }

    /** Devuelve el operador que está en esa posición de la lista de tokens,
     * PERO solo si ese operador es uno de los que le pasaste como argumento (SUM, SUBTRACT, etc.)
     * Si el token en esa posición no es un operador, o es un operador que no está en la lista que le pasaste, o la posición está fuera de rango — devuelve null en cualquiera de esos casos. (muy feo)
     **/

    private fun currentOperator(tokens: List<Token>, position: Int, operators: List<PrintScriptOperator>): Optional<PrintScriptOperator> {
        val type = tokens.getOrNull(position)?.type
        return if (type is Operator && type.operator in operators) Optional.of( type.operator) else Optional.empty()
    }

}
