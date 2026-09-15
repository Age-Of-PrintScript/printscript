package lexer

import domain.Either
import domain.Failure
import domain.Position
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
    // se guarda el proximo caracter a consumir
    private var peekedChar: Int? = null
    private var currentLine = 1
    private var currentColumn = 1

    // retorna -1 si llego al EOF (contrato de java.io.Reader).
    private fun peekNextChar(reader: Reader): Int {
        if (peekedChar == null) peekedChar = reader.read()
        return peekedChar!!
    }

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
            val currentPos = Position(currentLine, currentColumn)
            val chr = consumeChar(reader).toChar()

            val nextState =
                state.consume(chr).getOrReturn {
                    val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                    return Failure(errWithPos)
                }

            builder =
                builder.addChar(chr, currentPos).getOrReturn {
                    val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                    return Failure(errWithPos)
                }
            state = nextState

            if (chr == '\n') {
                currentLine++
                currentColumn = 1
            } else {
                currentColumn++
            }

            val nextInt = peekNextChar(reader)

            if (shouldCloseToken(nextInt, state)) {
                val token =
                    builder.build().getOrReturn {
                        val errWithPos = if (it.start == null) it.withPosition(currentPos, currentPos) else it
                        return Failure(errWithPos)
                    }
                return Success(token)
            }
        }

        return null
    }

    private fun shouldCloseToken(
        nextInt: Int,
        state: State,
    ): Boolean = nextInt == -1 || !state.canConsume(nextInt.toChar())

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
