package parser

import ast.ASTViejo
import ast.Program
import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import parser.states.Start
import parser.states.State
import parser.states.StatementComplete
import tokens.TokenListViejo

internal class ParserStateMachine {
    fun parse(
        tokens: TokenListViejo,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, Program> {
        var state: State = Start
        var builder = ASTBuilder()
        val trees = mutableListOf<ASTViejo>()

        for (token in tokens) {
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
        return finalizeParsing(state, trees.toList(), tokens)
    }

    private fun finalizeParsing(
        state: State,
        trees: List<ASTViejo>,
        tokens: TokenListViejo,
    ): Either<SyntaxError, Program> {
        if (state != Start) return Failure(SyntaxError.INCOMPLETE_STATEMENT)

        return Success(
            Program(
                trees,
                getInitialPosition(tokens),
                getFinalPosition(tokens),
            ),
        )
    }

    private fun getFinalPosition(tokens: TokenListViejo): Position = if (tokens.isNotEmpty()) tokens.last().end else Position(0, 0)

    private fun getInitialPosition(tokens: TokenListViejo): Position = if (tokens.isNotEmpty()) tokens.first().start else Position(0, 0)
}
