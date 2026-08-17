package parser.states

import domain.Either
import tokens.ASSIGN
import ast.ASTDataType
import ast.ASTIdentifier
import tokens.COLON
import domain.Failure
import tokens.SEMICOLON
import domain.Success
import ast.Expression
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import tokens.DataType
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.Token






// ---------- Rama DECLARATION ----------

internal object LetSeen : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (val t = token.type) {
            is Identifier -> {
                val b = builder as ASTBuilder.DeclarationBuilder
                Success(DeclarationIdSeen(ASTIdentifier(t.name)) to b.copy(id = ASTIdentifier(t.name)))
            }
            else -> Failure(SyntaxError.MISSING_IDENTIFIER)
        }
    }
}

internal data class DeclarationIdSeen(val id: ASTIdentifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            COLON -> Success(DeclarationColonSeen(id) to builder)
            else -> Failure(SyntaxError.MISSING_COLON_IN_DECLARATION)
        }
    }
}

internal data class DeclarationColonSeen(val id: ASTIdentifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (val t = token.type) {
            is DataType -> {
                val b = builder as ASTBuilder.DeclarationBuilder
                val type = ASTDataType(t.type)
                Success(DeclarationTypeSeen(id, type) to b.copy(type = type))
            }
            else -> Failure(SyntaxError.MISSING_TYPE_IN_DECLARATION)
        }
    }
}

internal data class DeclarationTypeSeen(val id: ASTIdentifier, val type: ASTDataType) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            SEMICOLON -> Success(StatementComplete to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN_AFTER_TYPE)
        }
    }
}

// ---------- Rama ASSIGNMENT ----------

internal data class AssignmentIdSeen(val id: ASTIdentifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            else -> Failure(SyntaxError.MISSING_ASSIGNMENT_OPERATOR)
        }
    }
}

// ---------- Punto en común: acumula tokens para el Expression Parser ----------

internal data class ExpressionPending(
    val id: ASTIdentifier,
    val tokens: List<Token> = emptyList()
) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            SEMICOLON -> {
                when(val result = expressionParser.parseExpression(tokens)){
                    is Failure ->  Failure(result.value)
                    is Success -> {
                        val newBuilder = addExpressionToBuilder(builder, result.value)
                        Success(StatementComplete to newBuilder)
                    }
                }
            }
            is Literal, is Identifier, is Operator ->
                Success(copy(tokens = tokens + token) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
    }
}

// ---------- Rama CALL ----------



// ---------- Estado de aceptación ----------

internal object StatementComplete : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return Failure(SyntaxError.UNEXPECTED_TOKEN_AFTER_STATEMENT)
    }
}


internal fun addExpressionToBuilder(builder: ASTBuilder, value: Expression): ASTBuilder {
    return when(builder){
        is ASTBuilder.AssignmentBuilder -> ASTBuilder.AssignmentBuilder(builder.id, value)
        is ASTBuilder.DeclarationBuilder -> ASTBuilder.DeclarationBuilder(builder.id, builder.type, value)
        is ASTBuilder.CallBuilder -> ASTBuilder.CallBuilder(builder.functionName, builder.expressions + value)
        else -> builder
    }
}
