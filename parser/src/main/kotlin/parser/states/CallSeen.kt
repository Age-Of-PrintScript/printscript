package parser.states

import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.OpenParenthesis
import tokens.SEMICOLON
import tokens.Token

internal data class CallSeen(val function: PrintScriptFunctions) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            OpenParenthesis -> Success(StatementComplete to builder)
            else -> Failure(SyntaxError.MISSING_SEMICOLON)
        }
    }
}