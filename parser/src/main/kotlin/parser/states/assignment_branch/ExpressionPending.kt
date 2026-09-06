package parser.states.assignment_branch

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import parser.states.StatementComplete
import tokens.CLOSED_PARENTHESIS
import tokens.Identifier
import tokens.Literal
import tokens.OPEN_PARENTHESIS
import tokens.Operator
import tokens.SEMICOLON
import tokens.TokenViejo

internal data class ExpressionPending(
    val id: ASTIdentifier,
    val tokenViejos: List<TokenViejo> = emptyList(),
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            SEMICOLON -> {
                buildExpressionOnAst(expressionParser, builder)
            }
            is Literal, is Identifier, is Operator, is OPEN_PARENTHESIS, is CLOSED_PARENTHESIS ->
                Success(copy(tokenViejos = tokenViejos + tokenViejo) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }

    private fun buildExpressionOnAst(
        expressionParser: ExpressionParser,
        builder: ASTBuilder,
    ): Either<SyntaxError, ConsumeResult> =
        when (val result = expressionParser.parseExpression(tokenViejos)) {
            is Failure -> Failure(result.value)
            is Success -> {
                val newBuilder = builder.addExpression(result.value)
                Success(StatementComplete to newBuilder)
            }
        }
}
