import states.InitialState

internal class Automata {
    private var state: State = InitialState()
    private val builder: TokenBuilder = TokenBuilder()

    fun tokenize(source: String): Either<LexerError, List<Token>> {
        val tokenList = mutableListOf<Token>()
        for (chr in source) {
            val result = state.consume(chr)
            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> handleSuccess(chr, result.value, tokenList)
            }
        }
        return Success(tokenList.toList())
    }

    private fun handleSuccess(
        chr: Char,
        result: StateResult,
        tokenList: MutableList<Token>
    ): Either<LexerError, Unit> {
        builder.addChar(chr)
        when (result) {
            is Next -> state = result.state
            is Done -> {
                val newToken = builder.build()
                when (newToken) {
                    is Failure -> return Failure(newToken.value)
                    is Success -> {
                        tokenList.add(newToken.value)
                        builder.reset()
                    }
                }
            }
        }
        return Success(Unit)
    }
}
