package parser.states

import domain.Either
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.Token

internal interface State {
    fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult>
}

internal typealias ConsumeResult = Pair<State, ASTBuilder>