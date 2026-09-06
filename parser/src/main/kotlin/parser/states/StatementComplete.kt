package parser.states

import domain.Either
import domain.Failure
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.TokenViejo

internal object StatementComplete : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> = Failure(SyntaxError.UNEXPECTED_TOKEN_AFTER_STATEMENT)
}
