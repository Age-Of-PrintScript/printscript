fun main(){
    val automata = ParserStateMachine()
    val tokensTypes = listOf(
        LET,
        Identifier("x"),
        COLON,
        DataType(PrintScriptType.NUMBER),
        SEMICOLON
    )
    val tokens = tokensTypes.map {
        Token (
            it,
            Position(0,0),
            Position(0,0),
        )
    }
    val res = automata.parse(tokens)
    println(res)
}