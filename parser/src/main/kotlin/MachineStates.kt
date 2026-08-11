import kotlin.math.exp

internal typealias ConsumeResult = Pair<State, ASTBuilder>


internal interface State {
    fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult>
}

// ---------- Start ----------

internal object Start : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (val t = token.type) {
            is Call -> Success(CallSeen(t.type) to ASTBuilder.CallBuilder(t.type.toString()))
            is Identifier -> Success(AssignmentIdSeen(Identifier(t.name)) to ASTBuilder.AssignmentBuilder(Identifier(t.name)))
            LET -> Success(LetSeen to ASTBuilder.DeclarationBuilder())
            else -> Failure(SINTAX_ERROR("Unexpected token"))
        }
    }
}

// ---------- Rama DECLARATION ----------

internal object LetSeen : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (val t = token.type) {
            is Identifier -> {
                val b = builder as ASTBuilder.DeclarationBuilder
                Success(DeclarationIdSeen(Identifier(t.name)) to b.copy(id = Identifier(t.name)))
            }
            else -> Failure(SINTAX_ERROR("Identifier expected"))
        }
    }
}

internal data class DeclarationIdSeen(val id: Identifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (token.type) {
            COLON -> Success(DeclarationColonSeen(id) to builder)
            else -> Failure(SINTAX_ERROR("Missing colon"))
        }
    }
}

internal data class DeclarationColonSeen(val id: Identifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (val t = token.type) {
            is DataType -> {
                val b = builder as ASTBuilder.DeclarationBuilder
                val type = DataType(t.type)
                Success(DeclarationTypeSeen(id, type) to b.copy(type = type))
            }
            else -> Failure(SINTAX_ERROR("Missing type declaration"))
        }
    }
}

internal data class DeclarationTypeSeen(val id: Identifier, val type: DataType) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            SEMICOLON -> Success(StatementComplete to builder)
            else -> Failure(SINTAX_ERROR("Unresolved reference"))
        }
    }
}

// ---------- Rama ASSIGNMENT ----------

internal data class AssignmentIdSeen(val id: Identifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            else -> Failure(SINTAX_ERROR("Expected assignment"))
        }
    }
}

// ---------- Punto en común: acumula tokens para el Expression Parser ----------

internal data class ExpressionPending(
    val id: Identifier,
    val tokens: List<Token> = emptyList()
) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (token.type) {
            SEMICOLON -> {
                val result = expressionParser.parseExpression(tokens)
                when(result){
                    is Failure ->  Failure(result.value)
                    is Success -> {
                        val newBuilder = addExpressionToBuilder(builder, result.value)
                        Success(StatementComplete to newBuilder)
                    }
                }
            }
            is Literal, is Identifier, is Operator ->
                Success(copy(tokens = tokens + token) to builder)
            else -> Failure(SINTAX_ERROR("Unexpected token in expression"))
        }
    }
}

// ---------- Rama CALL ----------

internal data class CallSeen(val function: PrintScriptFunctions) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError, ConsumeResult> {
        return when (token.type) {
            SEMICOLON -> Success(StatementComplete to builder)
            else -> Failure(SINTAX_ERROR("Expected semicolon after call"))
        }
    }
}

// ---------- Estado de aceptación ----------

internal object StatementComplete : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<ParsingError,ConsumeResult> {
        return Failure(SINTAX_ERROR("Unexpected token after end of statement"))
    }
}


internal fun addExpressionToBuilder(builder: ASTBuilder, value: Expression): ASTBuilder {
    return when(builder){
        is ASTBuilder.AssignmentBuilder -> ASTBuilder.AssignmentBuilder(builder.id, value)
        is ASTBuilder.DeclarationBuilder -> ASTBuilder.DeclarationBuilder(builder.id, builder.type, value)
        else -> builder
    }
}