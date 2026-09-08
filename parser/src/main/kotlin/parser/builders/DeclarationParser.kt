package parser.builders

import ast.AST
import domain.Either
import domain.Failure
import domain.PSType
import domain.Success
import domain.getOrReturn
import parser.ExpressionParser
import parser.SyntaxError
import parser.TokenConsumer
import tokens.Assign
import tokens.Colon
import tokens.Const
import tokens.DataType
import tokens.Identifier
import tokens.Let
import tokens.Semicolon
import tokens.Token
import tokens.TokenType

class DeclarationParser(
    private val expressionParser: ExpressionParser = ExpressionParser(),
) : StatementParser {
    override fun canParse(token: TokenType): Boolean = token is Let || token is Const

    override fun parse(consumer: TokenConsumer): Either<SyntaxError, AST> {
        if (!consumer.hasNext()) return Failure(SyntaxError.INCOMPLETE_STATEMENT)
        val keywordToken = consumer.consume()
        val isMutable = keywordToken.type is Let

        val idToken =
            consumer
                .consumeExpected(Identifier::class, SyntaxError.MISSING_IDENTIFIER)
                .getOrReturn { return Failure(it) }
        val id = (idToken.type as Identifier).name

        consumer
            .consumeExpected(Colon::class, SyntaxError.MISSING_COLON_IN_DECLARATION)
            .getOrReturn { return Failure(it) }

        val typeToken =
            consumer
                .consumeExpected(DataType::class, SyntaxError.MISSING_TYPE_IN_DECLARATION)
                .getOrReturn { return Failure(it) }
        val psType = (typeToken.type as DataType).name

        if (consumer.consumeIf(Semicolon::class) != null) {
            if (!isMutable) return Failure(SyntaxError.INVALID_TOKEN)
            return Success(AST.Declaration(id, psType, mutable = true, value = null))
        }

        consumer
            .consumeExpected(Assign::class, SyntaxError.INVALID_TOKEN_AFTER_TYPE)
            .getOrReturn { return Failure(it) }

        return parseAssignedValue(id, psType, isMutable, consumer)
    }

    private fun parseAssignedValue(
        id: String,
        type: PSType,
        isMutable: Boolean,
        consumer: TokenConsumer,
    ): Either<SyntaxError, AST> {
        val exprTokens = mutableListOf<Token>()
        while (consumer.hasNext() && consumer.peek().type !is Semicolon) {
            exprTokens.add(consumer.consume())
        }
        consumer
            .consumeExpected(Semicolon::class, SyntaxError.MISSING_SEMICOLON)
            .getOrReturn { return Failure(it) }

        val expr = expressionParser.parse(exprTokens).getOrReturn { return Failure(it) }
        return Success(AST.Declaration(id, type, mutable = isMutable, value = expr))
    }
}
