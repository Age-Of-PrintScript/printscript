package parser.states

import domain.Either
import parser.ASTBuilder
import parser.OldExpressionParser
import parser.SyntaxError
import tokens.TokenViejo

internal interface State {
    fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult>
}

internal typealias ConsumeResult = Pair<State, ASTBuilder>
