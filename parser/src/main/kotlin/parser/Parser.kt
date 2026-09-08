package parser

import ast.AST
import ast.ASTType
import ast.Program
import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import parser.builders.StatementParser
import tokens.Call
import tokens.Const
import tokens.Identifier
import tokens.Let
import tokens.Token
import tokens.TokenType
import tokens.Whitespace

interface Parser {
    fun parse(tokens: List<Token>): Either<SyntaxError, Program>

    companion object {
        fun new(statementParsers: Map<ASTType, StatementParser>): Parser = ParserImpl(statementParsers)
    }
}

internal class ParserImpl(
    private val statementParsers: Map<ASTType, StatementParser>,
) : Parser {
    override fun parse(tokens: List<Token>): Either<SyntaxError, Program> {
        val cleanTokens = tokens.filterNot { it.type is Whitespace }
        if (cleanTokens.isEmpty()) {
            return Success(Program(emptyList(), Position(0, 0), Position(0, 0)))
        }

        val consumer = TokenConsumer(cleanTokens)
        val statements = mutableListOf<AST>()

        while (consumer.hasNext()) {
            val nextToken = consumer.peek()
            val astType = getASTType(nextToken.type) ?: return Failure(SyntaxError.INVALID_TOKEN)
            val parser = statementParsers[astType] ?: return Failure(SyntaxError.INVALID_TOKEN)

            val statement = parser.parse(consumer).getOrReturn { return Failure(it) }
            statements.add(statement)
        }

        return Success(
            Program(
                trees = statements,
                start = cleanTokens.first().start,
                end = cleanTokens.last().end,
            ),
        )
    }

    private fun getASTType(tokenType: TokenType): ASTType? =
        when (tokenType) {
            is Let, is Const -> ASTType.DECLARATION
            is Identifier -> ASTType.ASSIGNMENT
            is Call -> ASTType.EXPRESSION_STATEMENT
            else -> null
        }
}
