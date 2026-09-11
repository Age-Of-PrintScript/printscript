package parser.builders

import ast.AST
import ast.Block
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import parser.BlockParser
import parser.ExpressionParser
import parser.SyntaxError
import parser.TokenConsumer
import tokens.CloseBraces
import tokens.Else
import tokens.If
import tokens.OpenBraces
import tokens.TokenType

class ConditionalParser(
    private val blockParser: BlockParser,
    private val expressionParser: ExpressionParser = ExpressionParser(),
) : StatementParser {
    override fun canParse(token: TokenType): Boolean = token is If

    override fun parse(consumer: TokenConsumer): Either<SyntaxError, AST> {
        if (!consumer.hasNext()) return Failure(SyntaxError.INCOMPLETE_STATEMENT)

        consumer
            .consumeExpected(If::class, SyntaxError.INVALID_TOKEN)
            .getOrReturn { return Failure(it) }

        val conditionTokens = consumer.consumeUntil(OpenBraces::class)
        val condition = expressionParser.parse(conditionTokens).getOrReturn { return Failure(it) }

        val ifBlock = parseBracedBlock(consumer).getOrReturn { return Failure(it) }

        if (consumer.consumeIf(Else::class) != null) {
            val elseBlock = parseBracedBlock(consumer).getOrReturn { return Failure(it) }
            return Success(AST.ConditionalStatement(condition, ifBlock, elseBlock))
        }

        return Success(AST.ConditionalStatement(condition, ifBlock, null))
    }

    private fun parseBracedBlock(consumer: TokenConsumer): Either<SyntaxError, Block> {
        consumer
            .consumeExpected(OpenBraces::class, SyntaxError.INVALID_TOKEN)
            .getOrReturn { return Failure(it) }

        val blockTokens = consumer.consumeUntil(CloseBraces::class)

        consumer
            .consumeExpected(CloseBraces::class, SyntaxError.INVALID_TOKEN)
            .getOrReturn { return Failure(it) }

        val statements = blockParser.parse(TokenConsumer(blockTokens)).getOrReturn { return Failure(it) }
        return Success(Block(statements))
    }
}
