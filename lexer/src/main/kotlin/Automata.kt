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

    private fun handleSuccess(chr: Char, result: StateResult, tokenList: MutableList<Token>) {
        builder.addChar(chr)
        when (result) {
            is Next -> state = result.state
            is Done -> {
                tokenList.add(builder.build())
                builder.reset()
            }
        }
    }
}