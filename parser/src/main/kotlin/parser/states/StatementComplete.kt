package parser.states

import domain.Either
import domain.Failure
import parser.ASTBuilder
import parser.OldExpressionParser
import parser.SyntaxError
import tokens.TokenViejo

internal object StatementComplete : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult> = Failure(SyntaxError.UNEXPECTED_TOKEN_AFTER_STATEMENT)
}
