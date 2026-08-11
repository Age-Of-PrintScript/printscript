interface Parser {
    fun parse(tokens: List<Token>): Either<ParsingError,Program>
}
class ParserImpl : Parser {
    override fun parse(tokens: List<Token>): Either<ParsingError,Program> {
        val result: Expression = parseExpression(tokens, 0).value
        //todo -> enchufar la expression al AST (lo de abajo es un dummy)
        TODO("Not yet implemented")
    }
}

private data class ParseResult<T>(val value: T, val nextPosition: Int)


private fun parseExpression(tokens: List<Token>, position: Int): ParseResult<Expression> {
    val term = parseTerm(tokens, position)
    return parseExpressionRec(tokens, term.nextPosition, term.value)
}

private fun parseTerm(tokens: List<Token>, position: Int): ParseResult<Expression> {
    val factor = parseFactor(tokens, position)
    return parseTermRec(tokens, factor.nextPosition, factor.value)
}

private fun parseFactor(tokens: List<Token>, position: Int): ParseResult<Expression> {
    val token = tokens.getOrNull(position)
        ?: throw IllegalStateException("Se esperaba un token en posicion $position, pero la lista termino")

    val expression = when (val type = token.type) {
        is Literal -> Expression.Literal(type.value)
        is Identifier -> Expression.Variable(type.name)
        else -> throw IllegalStateException("Se esperaba numero o variable, se encontro $type en posicion $position")
    }
    return ParseResult(expression, position + 1)
}

// métodos auxiliares recursivos

private tailrec fun parseExpressionRec(
    tokens: List<Token>,
    position: Int,
    left: Expression
): ParseResult<Expression> {
    val operator = currentOperator(tokens, position, termSeparators)
        ?: return ParseResult(left, position) //si es nulo, entonces no hay una expression. Me quedo con lo de la izquierda.

    val right = parseTerm(tokens, position + 1)
    return parseExpressionRec(tokens, right.nextPosition, Expression.Operation(left, right.value, operator))
}

private tailrec fun parseTermRec(
    tokens: List<Token>,
    position: Int,
    left: Expression
): ParseResult<Expression> {
    val operator = currentOperator(tokens, position, factorSeparators)
        ?: return ParseResult(left, position)

    val right = parseFactor(tokens, position + 1)
    return parseTermRec(tokens, right.nextPosition, Expression.Operation(left, right.value, operator))
}



/** Devuelve el operador que está en esa posición de la lista de tokens,
 * PERO solo si ese operador es uno de los que le pasaste como argumento (SUM, SUBTRACT, etc).
 * Si el token en esa posición no es un operador, o es un operador que no está en la lista que le pasaste, o la posición está fuera de rango — devuelve null en cualquiera de esos casos. (muy feo)
 **/


private fun currentOperator(tokens: List<Token>, position: Int, operators: List<PrintScriptOperator>): PrintScriptOperator? {
    val type = tokens.getOrNull(position)?.type
    return if (type is Operator && type.operator in operators) type.operator else null
}












