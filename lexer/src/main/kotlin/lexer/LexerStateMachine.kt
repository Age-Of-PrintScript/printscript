package lexer

import domain.Either
import domain.Failure
import domain.Success
import lexer.states.Done
import lexer.states.InitialState
import lexer.states.Next
import lexer.states.State
import tokens.Token

internal class LexerStateMachine {
    private var state: State = InitialState()
    private val builder: TokenBuilder = TokenBuilder()

    fun tokenize(source: String): Either<LexerError, List<Token>> {
        val tokenList = mutableListOf<Token>()

        var i = 0;

        while (i < source.length) {
            val chr = source[i]
            val result = state.consume(chr)
            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> {
                    when (result.value) {
                        is Next -> {
                            val r = builder.addChar(chr)
                            when (r) {
                                is Success -> {
                                    state = (result.value as Next).state
                                    i++
                                }
                                is Failure -> return Failure(r.value)
                            }
                        }
                        is Done -> {
                            val newToken = builder.build()
                            when (newToken) {
                                is Failure -> return Failure(newToken.value)
                                is Success -> {
                                    tokenList.add(newToken.value)
                                    builder.reset()
                                    state = InitialState()
                                }
                            }
                            TODO()
                        }
                    }


                }
            }
        }
        return Success(tokenList.toList())
    }

    private fun handleToken(
        newToken: Either<LexerError, Token>,
    ): Either<LexerError, Token> {
        when (newToken) {
            is Failure -> return Failure(newToken.value)
            is Success -> {
                builder.reset()
                return Success(newToken.value)
            }
        }
    }
}
