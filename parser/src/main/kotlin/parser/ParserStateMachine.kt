package parser

import domain.Either
import domain.Failure
import domain.Position
import ast.Program
import domain.Success
import ast.AST
import domain.getOrReturn
import parser.states.Start
import parser.states.State
import parser.states.StatementComplete
import tokens.TokenList

internal class ParserStateMachine {
    fun parse(tokens: TokenList, expressionParser: ExpressionParser): Either<SyntaxError, Program> {
        var state: State = Start
        var builder = ASTBuilder()
        val trees = mutableListOf<AST>()

        for(token in tokens){
            val result = state.consume(token, builder, expressionParser)

            val pair = result.getOrReturn { return Failure(it) }
            state = pair.first
            builder = pair.second

            if (state == StatementComplete) {
                val ast = builder.build().getOrReturn { return Failure(it) }
                trees.add(ast)
                state = Start
                builder = ASTBuilder()
            }
        }
        return finalizeParsing(state, trees, tokens)
    }

    private fun finalizeParsing(
        state: State,
        trees: List<AST>,
        tokens: TokenList
    ): Either<SyntaxError, Program> {
        if (state != Start) {
            return Failure(SyntaxError.INCOMPLETE_STATEMENT)
        }
        return Success(
            Program(
            trees,
            if(tokens.isNotEmpty()) tokens.first().start else Position(0, 0),
            if(tokens.isNotEmpty()) tokens.last().end else Position(0, 0)
        ))
    }
}
