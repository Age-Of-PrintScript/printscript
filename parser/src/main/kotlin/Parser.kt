interface Parser {
    fun parse(tokens: List<Token>): Either<ParsingError,Program>
}
class ParserImpl : Parser {
    override fun parse(tokens: List<Token>): Either<ParsingError,Program> {
        val result: Expression = parseExpression(tokens, 0).value
        //todo -> enchufar la expression al AST (lo de abajo es un dummy)
        TODO("Not yet implemented")
    }
}













