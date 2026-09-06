package parser

import ast.Expression
import domain.Either
import domain.Failure
import domain.PrintScriptOperator
import domain.PrintScriptValue.NumberLiteral
import domain.PrintScriptValue.StringLiteral
import domain.Success
import domain.factorSeparators
import domain.termSeparators
import tokens.CLOSED_PARENTHESISViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.TokenViejo
import java.util.Optional

internal class ExpressionParser {
    fun parseExpression(expression: List<TokenViejo>): Either<SyntaxError, Expression> =
        when (val result = createExpressionTree(expression)) {
            is Success -> Success(result.value.parsedResult)
            is Failure -> Failure(result.value)
        }

    private data class ParsedResult<Expression>(
        val parsedResult: Expression,
        val nextPosition: Int,
    )

    private fun createExpressionTree(tokenViejos: List<TokenViejo>): Either<SyntaxError, ParsedResult<Expression>> =
        when (val term = parseTerm(tokenViejos, 0)) {
            is Success -> separateExpression(tokenViejos, term.value.nextPosition, term.value.parsedResult) // Quedo muy raro el nombre
            is Failure -> Failure(term.value)
        }

    private fun parseTerm(
        tokenViejos: List<TokenViejo>,
        position: Int,
    ): Either<SyntaxError, ParsedResult<Expression>> =
        when (val factor = parseFactor(tokenViejos, position)) {
            is Success -> separateTerm(tokenViejos, factor.value.nextPosition, factor.value.parsedResult)
            is Failure -> Failure(factor.value)
        }

    private fun parseFactor(
        tokenViejos: List<TokenViejo>,
        position: Int,
    ): Either<SyntaxError, ParsedResult<Expression>> {
        val token =
            tokenViejos.getOrNull(position)
                ?: return Failure(SyntaxError.INCOMPLETE_STATEMENT) // Si llegue aca y la lista termino, la expresión no tiene sentido.

        return when (val type = token.type) {
            is LiteralViejo -> {
                val number = type.value.toDoubleOrNull()
                if (number != null) {
                    Success(
                        ParsedResult(
                            Expression.Literal(NumberLiteral(number)),
                            position + 1,
                        ),
                    )
                } else {
                    Success(
                        ParsedResult(
                            Expression.Literal(StringLiteral(type.value)),
                            position + 1,
                        ),
                    )
                }
            }
            is IdentifierViejo -> Success(ParsedResult(Expression.Variable(type.name), position + 1))
            is OPEN_PARENTHESISViejo -> parseParenthesisExpression(tokenViejos, position + 1)
            else -> Failure(SyntaxError.WRONG_TOKEN_TYPE) // Si hay un tokenType que no es de los dos de arriba, la expresión no tiene sentido.
        }
    }

    private fun parseParenthesisExpression(
        tokenViejos: List<TokenViejo>,
        position: Int,
    ): Either<SyntaxError, ParsedResult<Expression>> =
        when (val expression = createExpressionTree(tokenViejos.subList(position, tokenViejos.size))) {
            is Success -> checkClosingParenthesis(tokenViejos, position + expression.value.nextPosition, expression.value)
            is Failure -> expression
        }

    private fun checkClosingParenthesis(
        tokenViejos: List<TokenViejo>,
        position: Int,
        parsedExpression: ParsedResult<Expression>,
    ): Either<SyntaxError, ParsedResult<Expression>> =
        when (tokenViejos.getOrNull(position)?.type) {
            is CLOSED_PARENTHESISViejo -> Success(ParsedResult(parsedExpression.parsedResult, position + 1))
            else -> Failure(SyntaxError.MISSING_CLOSING_PARENTHESIS)
        }

// métodos auxiliares recursivos

    private tailrec fun separateExpression(
        tokenViejos: List<TokenViejo>,
        position: Int,
        left: Expression,
    ): Either<SyntaxError, ParsedResult<Expression>> {
        val operator = currentOperator(tokenViejos, position, termSeparators)
        if (operator.isEmpty) return Success(ParsedResult(left, position)) // Si es empty, no estoy en el medio de una expresión. Me quedo con lo de la izquierda.

        return when (val right = parseTerm(tokenViejos, position + 1)) { // obtengo el otro miembro de la expresión
            is Success ->
                separateExpression(
                    tokenViejos,
                    right.value.nextPosition,
                    Expression.Operation(left, right.value.parsedResult, operator.get()),
                )
            is Failure -> Failure(right.value)
        }
    }

    private tailrec fun separateTerm(
        tokenViejos: List<TokenViejo>,
        position: Int,
        left: Expression,
    ): Either<SyntaxError, ParsedResult<Expression>> {
        val operator = currentOperator(tokenViejos, position, factorSeparators)
        if (operator.isEmpty) return Success(ParsedResult(left, position)) // Si es empty, no estoy en el medio de un termino. Me quedo con lo de la izquierda.

        return when (val right = parseFactor(tokenViejos, position + 1)) { // obtengo el otro miembro del término
            is Success ->
                separateTerm(
                    tokenViejos,
                    right.value.nextPosition,
                    Expression.Operation(left, right.value.parsedResult, operator.get()),
                )
            is Failure -> Failure(right.value)
        }
    }

    /** Devuelve el operador que está en esa posición de la lista de tokens,
     * PERO solo si ese operador es uno de los que le pasaste como argumento (SUM, SUBTRACT, etc.)
     * Si el token en esa posición no es un operador, o es un operador que no está en la lista que le pasaste, o la posición está fuera de rango — devuelve null en cualquiera de esos casos. (muy feo)
     **/

    private fun currentOperator(
        tokenViejos: List<TokenViejo>,
        position: Int,
        operators: List<PrintScriptOperator>,
    ): Optional<PrintScriptOperator> {
        val type = tokenViejos.getOrNull(position)?.type
        return if (type is OperatorViejo && type.operator in operators) Optional.of(type.operator) else Optional.empty()
    }
}
