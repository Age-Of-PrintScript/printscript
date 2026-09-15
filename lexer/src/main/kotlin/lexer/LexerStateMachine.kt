package lexer

import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import lexer.states.InitialState
import lexer.states.State
import tokens.Token
import tokens.Whitespace
import java.io.Reader

internal class LexerStateMachine(
    val lexicon: Lexicon,
) {
    // Buffer de lookahead de 1 carácter.
    private var peekedChar: Int? = null

    // Retorna -1 si llegó al EOF (contrato de java.io.Reader).
    private fun peekNextChar(reader: Reader): Int {
        if (peekedChar == null) peekedChar = reader.read()
        return peekedChar!!
    }

    // Marca el char del buffer como consumido y lo retorna.
    private fun consumeChar(reader: Reader): Int {
        val ch = peekNextChar(reader)
        peekedChar = null
        return ch
    }

    // DFA puro: lee caracteres hasta cerrar UN token (puede ser Whitespace).
    // Retorna null si el stream está vacío (EOF).
    private fun readRawToken(reader: Reader): Either<LexerError, Token>? {
        if (peekNextChar(reader) == -1) return null

        var state: State = InitialState(lexicon)
        var builder = TokenBuilder(lexicon)

        while (peekNextChar(reader) != -1) {
            val chr = consumeChar(reader).toChar()

            val nextState = state.consume(chr).getOrReturn { return Failure(it) }
            builder = builder.addChar(chr).getOrReturn { return Failure(it) }
            state = nextState

            val nextInt = peekNextChar(reader)
            val shouldCloseToken = nextInt == -1 || !state.canConsume(nextInt.toChar())

            if (shouldCloseToken) {
                return builder.build()
            }
        }

        return null
    }

    // Devuelve el próximo token no-whitespace, o null si llegó al EOF.
    // Equivale al .filter { it.type != Whitespace } del tokenize original.
    fun nextToken(reader: Reader): Either<LexerError, Token>? {
        var raw = readRawToken(reader)
        while (raw != null) {
            val token = raw.getOrReturn { return Failure(it) }
            if (token.type !is Whitespace) return Success(token)
            raw = readRawToken(reader)
        }
        return null
    }
}
