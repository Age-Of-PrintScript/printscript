package lexer

import domain.Failure
import domain.Position
import domain.Success
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import tokens.TokenTypeViejo
import tokens.TokenViejo

internal fun assertCorrectSource(
    lexer: Lexer,
    input: String,
    expected: List<TokenTypeViejo>,
) {
    val result = lexer.tokenize(input)
    assertTrue(result is Success, "Tokenization wasn't successful")
    val givenTokenViejos = (result as Success<LexerError, List<TokenViejo>>).value
    assertEqualTokenList(createTokens(expected), givenTokenViejos)
}

internal fun assertEqualTokenList(
    expected: List<TokenViejo>,
    actual: List<TokenViejo>,
) {
    if (expected.size != actual.size) error("Expected ${expected.size} tokenlist size || Actual ${actual.size} token list size")
    for (i in expected.indices) {
        assertEquals(expected[i], actual[i], "Expected: ${expected[i]} || Actual: ${actual[i]}")
    }
}

internal fun assertIncorrectSource(
    lexer: Lexer,
    input: String,
    expected: LexerError,
) {
    val result = lexer.tokenize(input)
    assertTrue(result is Failure, "Tokenization shouldn't have succeeded")
    assertEquals(expected, (result as Failure<LexerError, List<TokenViejo>>).value)
}

internal fun createTokens(types: List<TokenTypeViejo>): List<TokenViejo> =
    types.map {
        TokenViejo(
            it,
            Position(0, 0),
            Position(0, 0),
        )
    }
