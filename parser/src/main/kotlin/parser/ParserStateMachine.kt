package parser

import domain.Either
import domain.Failure
import domain.Position
import ast.Program
import domain.Success
import ast.AST
import tokens.TokenList

internal class ParserStateMachine {
    fun parse(tokens: TokenList, expressionParser: ExpressionParser): Either<SyntaxError, Program> {
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