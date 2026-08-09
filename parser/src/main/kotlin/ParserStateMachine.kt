internal class ParserStateMachine {
}

internal interface State{
    fun consume(token: Token): Either<SINTAX_ERROR,State>
}

internal object Start : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        return when(token.type){
            is Call -> TODO()
            is Identifier -> TODO()
            LET -> Success(LetSeen)
            else -> Failure(SINTAX_ERROR())
        }
    }
}
internal object LetSeen : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        when(token.type){
            is Identifier -> TODO()
            else -> return Failure(SINTAX_ERROR("Identifier expected"))
        }
    }
}
internal data class DeclarationIdSeen(val id: Identifier) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return when (token.type) {
            COLON -> TODO()
            else -> Failure(SINTAX_ERROR("Missing colon"))
        }
    }
}
internal data class DeclarationColonSeen(val id: Identifier) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return when (token.type) {
            is DataType -> TODO()
            else -> Failure(SINTAX_ERROR("Missing type declaration"))
        }
    }
}


internal data class DeclarationTypeSeen(val id: Identifier, val type: DataType) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return when (token.type) {
            ASSIGN -> TODO()
            SEMICOLON -> TODO()
            else -> Failure(SINTAX_ERROR("Unresolved reference"))
        }
    }
}
internal data class AssignmentIdSeen(val id: Identifier) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return when (token.type) {
            ASSIGN -> TODO()
            else -> Failure(SINTAX_ERROR("Expected assignment"))
        }
    }
}
internal data class ExpressionPending(val id: Identifier, val type: DataType?) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        TODO()
    }
}

internal data class CallSeen(val function: PrintScriptFunctions) : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return when (token.type) {
            SEMICOLON -> TODO()
            else -> Failure(SINTAX_ERROR("Expected semicolon after call"))
        }
    }
}
internal object StatementComplete : State {
    override fun consume(token: Token): Either<SINTAX_ERROR, State> {
        return Failure(SINTAX_ERROR("Unexpected token after end of statement"))
    }
}