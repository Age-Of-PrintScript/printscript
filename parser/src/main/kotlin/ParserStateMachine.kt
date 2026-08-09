internal class ParserStateMachine {
}

internal interface State{
    fun consume(token: Token): Either<SINTAX_ERROR,State>
}

//THIS IS THE INITIAL STATE OF THE MACHINE
internal object Q0 : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        return when(token.type){
            is Call -> TODO()
            is Identifier -> TODO()
            LET -> Success(Q1)
            else -> Failure(SINTAX_ERROR())
        }
    }
}
internal object Q1 : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        when(token.type){
            is Identifier -> TODO()
            else -> return Failure(SINTAX_ERROR("Identifier expected"))
        }
    }
}
internal object Q2 : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        when(token.type){
            is COLON -> TODO()
            else -> return Failure(SINTAX_ERROR("Missing colon"))
        }
    }
}
internal object Q3 : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        when(token.type){
            is DataType -> TODO()
            else -> return Failure(SINTAX_ERROR("Missing type declaration"))
        }
    }
}

internal object Q4 : State{
    override fun consume(token: Token): Either<SINTAX_ERROR,State> {
        when(token.type){
            is ASSIGN -> TODO()
            is SEMICOLON -> TODO()
            else -> return Failure(SINTAX_ERROR("Unresolved reference"))
        }
    }
}