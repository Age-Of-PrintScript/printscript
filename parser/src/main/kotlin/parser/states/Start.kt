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
import tokens.CallViejo
import tokens.IdentifierViejo
import tokens.LETViejo
import tokens.TokenViejo

internal object Start : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (val t = tokenViejo.type) {
            is CallViejo -> Success(CallSeen(t.type) to ASTBuilder(type = BuilderType.CALL, functionName = t.type))
            is IdentifierViejo ->
                Success(
                    AssignmentIdSeen(ASTIdentifier(t.name)) to ASTBuilder(type = BuilderType.ASSIGNMENT, id = ASTIdentifier(t.name)),
                )
            LETViejo -> Success(DeclarationBranch to ASTBuilder(type = BuilderType.DECLARATION))
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
}
