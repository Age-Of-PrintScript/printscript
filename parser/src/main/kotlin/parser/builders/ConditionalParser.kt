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
import parser.tokenConsumers.TokenConsumer
import parser.tokenConsumers.consumeBalancedUntil
import parser.tokenConsumers.consumeExpected
import parser.tokenConsumers.consumeIf
import parser.tokenConsumers.consumeUntil
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

        val ifToken =
            consumer
                .consumeExpected(If::class, SyntaxError.INVALID_TOKEN)
                .getOrReturn { return Failure(it) }

        val conditionTokens = consumer.consumeUntil(OpenBraces::class)
        val condition = expressionParser.parse(conditionTokens).getOrReturn { return Failure(it) }

        val (ifBlock, ifEnd) = parseBracedBlock(consumer).getOrReturn { return Failure(it) }

        if (consumer.consumeIf(Else::class) != null) {
            val (elseBlock, elseEnd) = parseBracedBlock(consumer).getOrReturn { return Failure(it) }
            return Success(
                AST.ConditionalStatement(
                    condition = condition,
                    ifBlock = ifBlock,
                    elseBlock = elseBlock,
                    start = ifToken.start,
                    end = elseEnd,
                ),
            )
        }

        return Success(
            AST.ConditionalStatement(
                condition = condition,
                ifBlock = ifBlock,
                elseBlock = null,
                start = ifToken.start,
                end = ifEnd,
            ),
        )
    }

    private fun parseBracedBlock(consumer: TokenConsumer): Either<SyntaxError, Pair<Block, domain.Position>> {
        consumer
            .consumeExpected(OpenBraces::class, SyntaxError.INVALID_TOKEN)
            .getOrReturn { return Failure(it) }

        val blockTokens = consumer.consumeBalancedUntil(OpenBraces::class, CloseBraces::class)

        val closeBraceToken =
            consumer
                .consumeExpected(CloseBraces::class, SyntaxError.INVALID_TOKEN)
                .getOrReturn { return Failure(it) }

        val statements = blockParser.parse(TokenConsumer(blockTokens)).getOrReturn { return Failure(it) }
        return Success(Block(statements) to closeBraceToken.end)
    }
}
