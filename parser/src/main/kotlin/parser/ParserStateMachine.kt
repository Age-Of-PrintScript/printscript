package parser

import Failure
import Position
import ast.Program
import Success

internal class ParserStateMachine {
    fun parse(tokens: TokenList, expressionParser: ExpressionParser): Either<ParsingError, Program> {
        var state: State = Start
        var builder: ASTBuilder = ASTBuilder.EmptyBuilder
        val trees = mutableListOf<AST>()

        for(token in tokens){
            val result = state.consume(token, builder, expressionParser)
            when(result){
                is Failure -> return Failure(result.value)
                is Success -> {
                    state = result.value.first
                    builder = result.value.second
                    if (state == StatementComplete) {
                        when (val ast = builder.build()) {
                            is Failure -> return Failure(ast.value)
                            is Success -> trees.add(ast.value)
                        }
                        state = Start
                        builder = ASTBuilder.EmptyBuilder
                    }
                }
            }
        }
        return finalizeParsing(state, trees, tokens)
    }
    private fun finalizeParsing(
        state: State,
        trees: List<AST>,
        tokens: TokenList
    ): Either<ParsingError, Program> {
        if (state != Start) {
            return Failure(SYNTAX_ERROR("Unexpected end of input, incomplete statement"))
        }
        return Success(
            Program(
            trees,
            if(tokens.isNotEmpty()) tokens.first().start else Position(0, 0),
            if(tokens.isNotEmpty()) tokens.last().end else Position(0, 0)
        ))
    }
}