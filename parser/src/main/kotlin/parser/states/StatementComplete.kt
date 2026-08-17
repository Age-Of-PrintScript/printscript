package parser.states

import domain.Either
import domain.Failure
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.Token

internal object StatementComplete : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return Failure(SyntaxError.UNEXPECTED_TOKEN_AFTER_STATEMENT)
    }
}