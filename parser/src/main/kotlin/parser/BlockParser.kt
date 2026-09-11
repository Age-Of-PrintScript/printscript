package parser

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import parser.builders.StatementParser

data class BlockParser(
    val statementParsers: List<StatementParser>,
) {
    fun parse(consumer: TokenConsumer): Either<SyntaxError, List<AST>> {
        val statements = mutableListOf<AST>()

        while (consumer.hasNext()) {
            val nextToken = consumer.peek()
            val parser =
                statementParsers.firstOrNull { it.canParse(nextToken.type) }
                    ?: return Failure(SyntaxError.INVALID_TOKEN)

            val statement = parser.parse(consumer).getOrReturn { return Failure(it) }
            statements.add(statement)
        }
        return Success(statements.toList())
    }
}
