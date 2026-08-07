interface Parser {
    fun parse(tokens: List<Token>): Either<ParsingError,Program>
}
class ParserImpl : Parser {
    override fun parse(tokens: List<Token>): Either<ParsingError,Program> {
        TODO("Not yet implemented")
    }
}

