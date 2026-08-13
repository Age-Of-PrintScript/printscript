package lexer

import domain.Either
import domain.Failure
import domain.Success
import lexer.states.InitialState
import lexer.states.State
import tokens.Token
import tokens.TokenList
import tokens.WHITESPACE

internal class LexerStateMachine {
    private var state: State = InitialState()
    private val builder: TokenBuilder = TokenBuilder()

    fun tokenize(source: String): Either<LexerError, TokenList> {
        val tokenList = mutableListOf<Token>()
        for(i in source.indices) {
            val chr = source[i]
            val result = state.consume(chr)
            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> {
                    when(val appendResult = builder.addChar(chr)){
                        is Failure -> return Failure(appendResult.value)
                        is Success -> {
                            state = result.value

                            val isLastChar = i == source.length - 1
                            val nextChar = if (!isLastChar) source[i + 1] else null

                            val shouldCloseToken = nextChar == null || !state.canConsume(nextChar)

                            if (shouldCloseToken) {
                                when(val buildResult= builder.build()){
                                    is Failure -> return Failure(buildResult.value)
                                    is Success -> {
                                        tokenList.add(buildResult.value)
                                        builder.reset()
                                        state = InitialState()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Success(tokenList.filter { it.type != WHITESPACE })

    }

}
