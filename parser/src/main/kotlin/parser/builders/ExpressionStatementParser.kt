package parser.builders

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import parser.ExpressionParser
import parser.SyntaxError
import parser.TokenConsumer
import tokens.Call
import tokens.Semicolon
import tokens.TokenType

class ExpressionStatementParser(
    private val expressionParser: ExpressionParser = ExpressionParser(),
) : StatementParser {
    override fun canParse(token: TokenType): Boolean = token is Call

    override fun parse(consumer: TokenConsumer): Either<SyntaxError, AST> {
        val exprTokens = consumer.consumeUntil(Semicolon::class)
        consumer
            .consumeExpected(Semicolon::class, SyntaxError.MISSING_SEMICOLON)
            .getOrReturn { return Failure(it) }

        val expr = expressionParser.parse(exprTokens).getOrReturn { return Failure(it) }
        return Success(AST.ExpressionStatement(expr))
    }
}
