package parser.states

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.Call
import tokens.Identifier
import tokens.LET
import tokens.Token

internal object Start : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (val t = token.type) {
            is Call -> Success(CallSeen(t.type) to ASTBuilder.CallBuilder(t.type))
            is Identifier -> Success(AssignmentIdSeen(ASTIdentifier(t.name)) to ASTBuilder.AssignmentBuilder(ASTIdentifier(t.name)))
            LET -> Success(LetSeen to ASTBuilder.DeclarationBuilder())
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
    }
}