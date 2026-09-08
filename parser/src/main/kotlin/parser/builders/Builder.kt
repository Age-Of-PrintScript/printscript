package parser.builders

import ast.AST
import domain.Either
import parser.SyntaxError
import tokens.TokenList
import tokens.TokenType

interface Builder {
    fun canParse(token: TokenType): Boolean

    fun parse(tokens: TokenList): Either<SyntaxError, AST>
}
