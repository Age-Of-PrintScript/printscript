package lexer

import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import tokens.Token
import tokens.TokenList
import java.io.Reader
import java.io.StringReader

interface Lexer {
    fun nextToken(reader: Reader): Either<LexerError, Token>?

    fun tokenize(source: String): Either<LexerError, TokenList>

    companion object {
        fun new(lexicon: Lexicon): Lexer = LexerImpl(lexicon)
    }
}

internal class LexerImpl(
    val lexicon: Lexicon,
) : Lexer {
    private val stateMachine = LexerStateMachine(lexicon)

    override fun nextToken(reader: Reader): Either<LexerError, Token>? = stateMachine.nextToken(reader)

    override fun tokenize(source: String): Either<LexerError, TokenList> {
        // Creamos una instancia fresca del state machine para cada tokenización batch.
        // Esto evita que el estado peekedChar=-1 de un Reader anterior contamine el siguiente.
        val freshMachine = LexerStateMachine(lexicon)
        val reader = StringReader(source)
        val tokens = mutableListOf<Token>()
        while (true) {
            val result = freshMachine.nextToken(reader) ?: break
            val token = result.getOrReturn { return Failure(it) }
            tokens.add(token)
        }
        return Success(tokens)
    }
}
