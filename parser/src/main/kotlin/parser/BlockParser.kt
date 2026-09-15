package parser

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import parser.builders.StatementParser
import parser.tokenConsumers.TokenConsumer

data class BlockParser(
    val statementParsers: List<StatementParser>,
) {
    fun parseNext(consumer: TokenConsumer): Either<SyntaxError, AST>? {
        if (!consumer.hasNext()) return null
        val nextToken = consumer.peek()
        val parser =
            statementParsers.firstOrNull { it.canParse(nextToken.type) }
                ?: return Failure(SyntaxError.INVALID_TOKEN)

        return parser.parse(consumer)
    }

    fun parse(consumer: TokenConsumer): Either<SyntaxError, List<AST>> {
        val statements = mutableListOf<AST>()

        while (consumer.hasNext()) {
            val statement = parseNext(consumer)?.getOrReturn { return Failure(it) } ?: break
            statements.add(statement)
        }
        return Success(statements.toList())
    }
}
