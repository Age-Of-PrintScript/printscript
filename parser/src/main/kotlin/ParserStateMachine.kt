internal class ParserStateMachine {
    fun parse(tokens: TokenList): Either<ParsingError, Program>{
        var state: State = Start
        var builder: ASTBuilder = ASTBuilder.EmptyBuilder
        val trees = mutableListOf<AST>()

        for(token in tokens){
            val result = state.consume(token, builder)
            when(result){
                is Failure -> return Failure(result.value)
                is Success -> {
                    state = result.value.first
                    builder = result.value.second
                    if (state == StatementComplete) {
                        when (val ast = builder.build()) {
                            is Failure -> return Failure(ast.value)
                            is Success -> trees.add(ast.value)
                        }
                        state = Start
                        builder = ASTBuilder.EmptyBuilder
                    }
                }
            }
        }
        if (state != Start) {
            return Failure(SINTAX_ERROR("Unexpected end of input, incomplete statement"))
        }
        return Success(Program(trees, tokens.first().start, tokens.last().end))
    }
}