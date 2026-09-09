package parser.builders

import ast.AST
import domain.Either
import parser.SyntaxError
import parser.TokenConsumer
import tokens.TokenType

interface StatementParser {
    fun canParse(token: TokenType): Boolean

    fun parse(consumer: TokenConsumer): Either<SyntaxError, AST>
}
