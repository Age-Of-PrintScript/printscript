package parser.states

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.BuilderType
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.assignment_branch.AssignmentIdSeen
import parser.states.call_branch.CallSeen
import parser.states.declaration_branch.DeclarationBranch
import tokens.Call
import tokens.Identifier
import tokens.LET
import tokens.Token

internal object Start : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (val t = token.type) {
            is Call -> Success(CallSeen(t.type) to ASTBuilder(type = BuilderType.CALL, functionName = t.type))
            is Identifier ->
                Success(
                    AssignmentIdSeen(ASTIdentifier(t.name)) to ASTBuilder(type = BuilderType.ASSIGNMENT, id = ASTIdentifier(t.name)),
                )
            LET -> Success(DeclarationBranch to ASTBuilder(type = BuilderType.DECLARATION))
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
}
