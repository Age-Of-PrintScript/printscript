package parser.states.assignment_branch

import domain.Either
import ast.ASTIdentifier
import domain.Failure
import tokens.SEMICOLON
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import parser.states.StatementComplete
import tokens.CLOSED_PARENTHESIS
import tokens.OPEN_PARENTHESIS
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.Token

internal data class ExpressionPending(
    val id: ASTIdentifier,
    val tokens: List<Token> = emptyList()
) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            SEMICOLON -> {
                buildExpressionOnAst(expressionParser, builder)
            }
            is Literal, is Identifier, is Operator, is OPEN_PARENTHESIS, is CLOSED_PARENTHESIS ->
                Success(copy(tokens = tokens + token) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
    }

    private fun buildExpressionOnAst(
        expressionParser: ExpressionParser,
        builder: ASTBuilder
    ): Either<SyntaxError, ConsumeResult> = when (val result = expressionParser.parseExpression(tokens)) {
        is Failure -> Failure(result.value)
        is Success -> {
            val newBuilder = builder.addExpression(result.value)
            Success(StatementComplete to newBuilder)
        }
    }
}
