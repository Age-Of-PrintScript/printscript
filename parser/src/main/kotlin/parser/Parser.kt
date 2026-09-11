package parser

import ast.Program
import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import tokens.Token
import tokens.Whitespace

interface Parser {
    fun parse(tokens: List<Token>): Either<SyntaxError, Program>

    companion object {
        fun new(blockParser: BlockParser): Parser = ParserImpl(blockParser)
    }
}

internal class ParserImpl(
    private val blockParser: BlockParser,
) : Parser {
    override fun parse(tokens: List<Token>): Either<SyntaxError, Program> {
        val cleanTokens = tokens.filterNot { it.type is Whitespace }
        if (cleanTokens.isEmpty()) {
            return Success(Program(emptyList(), Position(0, 0), Position(0, 0)))
        }

        val consumer = TokenConsumer(cleanTokens)

        val statements = blockParser.parse(consumer).getOrReturn { return Failure(it) }

        return Success(
            Program(
                trees = statements,
                start = cleanTokens.first().start,
                end = cleanTokens.last().end,
            ),
        )
    }
}
