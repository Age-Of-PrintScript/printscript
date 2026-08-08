internal class ParserStateMachine {

}

internal interface State{
    fun consume(token: Token): State?
}

//THIS IS THE INITIAL STATE OF THE MACHINE
internal object Q0 : State{
    override fun consume(token: Token): State? {
        return when(token.type){
            is Call -> TODO()
            is Identifier -> TODO()
            LET -> Q1
            else -> null
        }
    }
}
internal object Q1 : State{
    override fun consume(token: Token): State? {
        when(token.type){

        }
    }
}