package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import lexer.states.InitialState
import lexer.states.State
import tokens.Token
import tokens.TokenList
import tokens.Whitespace

internal class LexerStateMachine(
    val lexicon: Lexicon,
) {
    fun tokenize(source: String): Either<LexerError, TokenList> {
        var state: State = InitialState(lexicon)
        var builder = TokenBuilder(lexicon)

        val tokenList = mutableListOf<Token>()

        var currentLine = 1
        var currentColumn = 1

        for (i in source.indices) {
            val chr = source[i]
            val currentPos = Position(currentLine, currentColumn)

            val result = state.consume(chr)
            val newState =
                result.getOrReturn {
                    val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                    return Failure(errWithPos)
                }

            builder =
                builder.addChar(chr, currentPos).getOrReturn {
                    val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                    return Failure(errWithPos)
                }
            state = newState

            val shouldCloseToken = cannotConsumeNextChar(i, source, state)

            if (shouldCloseToken) {
                val token =
                    builder.build().getOrReturn {
                        val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                        return Failure(errWithPos)
                    }
                tokenList.add(token)
                builder = TokenBuilder(lexicon)
                state = InitialState(lexicon)
            }

            if (chr == '\n') {
                currentLine++
                currentColumn = 1
            } else {
                currentColumn++
            }
        }
        return Success(tokenList.filter { it.type != Whitespace })
    }

    private fun cannotConsumeNextChar(
        i: Int,
        source: String,
        state: State,
    ): Boolean {
        val isLastChar = (i == source.length - 1)
        val nextChar = if (!isLastChar) source[i + 1] else null
        return nextChar == null || !state.canConsume(nextChar)
    }
}
