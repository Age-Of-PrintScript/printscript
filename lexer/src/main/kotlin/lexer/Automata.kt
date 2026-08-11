package lexer

import Either
import Failure
import Success
import Token
import lexer.states.Done
import lexer.states.Next
import lexer.states.State
import lexer.states.StateResult
import kotlin.text.iterator

internal class Automata {
    private var state: State = _root_ide_package_.lexer.states.InitialState()
    private val builder: TokenBuilder = TokenBuilder()

    fun tokenize(source: String): Either<LexerError, List<Token>> {
        val tokenList = mutableListOf<Token>()
        for (chr in source) {
            val result = state.consume(chr)
            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> {
                    val handled = handleSuccess(chr, result.value, tokenList)
                    if (handled is Failure) return Failure(handled.value)
                }
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
