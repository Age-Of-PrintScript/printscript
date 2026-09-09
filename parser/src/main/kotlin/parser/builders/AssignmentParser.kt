package parser.builders

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import parser.ExpressionParser
import parser.SyntaxError
import parser.TokenConsumer
import tokens.Assign
import tokens.Identifier
import tokens.Semicolon
import tokens.TokenType

class AssignmentParser(
    private val expressionParser: ExpressionParser = ExpressionParser(),
) : StatementParser {
    override fun canParse(token: TokenType): Boolean = token is Identifier

    override fun parse(consumer: TokenConsumer): Either<SyntaxError, AST> {
        val idToken =
            consumer
                .consumeExpected(Identifier::class, SyntaxError.MISSING_IDENTIFIER)
                .getOrReturn { return Failure(it) }
        val id = (idToken.type as Identifier).name

        consumer
            .consumeExpected(Assign::class, SyntaxError.MISSING_ASSIGNMENT_OPERATOR)
            .getOrReturn { return Failure(it) }

        val exprTokens = consumer.consumeUntil(Semicolon::class)
        consumer
            .consumeExpected(Semicolon::class, SyntaxError.MISSING_SEMICOLON)
            .getOrReturn { return Failure(it) }

        val expr = expressionParser.parse(exprTokens).getOrReturn { return Failure(it) }
        return Success(AST.AssignmentStatement(id, expr))
    }
}
