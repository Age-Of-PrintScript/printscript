package lexer

import domain.Either
import domain.Failure
import domain.PrintScriptSymbols
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
        TODO()
    }

}
