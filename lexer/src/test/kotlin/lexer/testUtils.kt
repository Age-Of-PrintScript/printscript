package lexer

import domain.Failure
import domain.Position
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import tokens.Token
import tokens.TokenType

internal fun assertCorrectSource(lexer: Lexer, input: String, expected: List<TokenType>){
    val result = lexer.tokenize(input)
    assertTrue(result is Success, "Tokenization wasn't successful")
    val givenTokens = (result as Success<LexerError, List<Token>>).value
    assertEqualTokenList(createTokens(expected), givenTokens)
}
internal fun assertEqualTokenList(expected: List<Token>, actual: List<Token>){
    if (expected.size != actual.size) error("Expected ${expected.size} tokenlist size || Actual ${actual.size} token list size")
    for(i in expected.indices){
        assertEquals(expected[i], actual[i], "Expected: ${expected[i]} || Actual: ${actual[i]}")
    }
}
internal fun assertIncorrectSource(lexer: Lexer, input: String, expected: LexerError){
    val result = lexer.tokenize(input)
    assertTrue(result is Failure, "Tokenization shouldn't have succeeded")
    assertEquals(expected, (result as Failure<LexerError, List<Token>>).value)
}
internal fun createTokens(types: List<TokenType>): List<Token> {
    return types.map{
        Token(
            it,
            Position(0,0),
            Position(0,0)
        )
    }
}