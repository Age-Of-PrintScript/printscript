package parser.states

import domain.Either
import domain.PrintScriptFunctions
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.ClosedParenthesis
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.Token

internal class CallArgsPending(
    val function: PrintScriptFunctions,
    val tokens: List<Token>,
): State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser
    ): Either<SyntaxError, ConsumeResult> {
        when(token.type) {
            is Literal, is Identifier, is Operator ->
                Success(copy(tokens = tokens + token) to builder)
            is ClosedParenthesis ->{

            }
            else -> {}
        }
    }
}