package lexer

import domain.Either
import domain.Failure
import domain.Success
import lexer.states.Done
import lexer.states.InitialState
import lexer.states.Next
import lexer.states.State
import tokens.Token
import kotlin.text.iterator

internal class LexerStateMachine {
    private var state: State = InitialState()
    private val builder: TokenBuilder = TokenBuilder()

    fun tokenize(source: String): Either<LexerError, List<Token>> {
        val tokenList = mutableListOf<Token>()

        for (chr in source) {
            val result = state.consume(chr)
            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> {
                    builder.addChar(chr)
                    when (result.value) {
                        is Next -> state = (result.value as Next).state
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
