package formattest.cases

object FormatterFailureCases {
    fun cases(): List<FormatterFailureCase> =
        listOf(
            FormatterFailureCase(
                name = "unknown printscript version returns FormatError",
                version = "9.9",
                source = "let x: number = 5;",
                configJson = "{}",
                expectedErrorMessage = "Unknown version",
            ),
            FormatterFailureCase(
                name = "invalid json in config file returns FormatError",
                version = "1.0",
                source = "let x: number = 5;",
                configJson = "{ invalid json content }",
                expectedErrorMessage = "Formatting rules are invalid",
            ),
            FormatterFailureCase(
                name = "syntax error in source script returns FormatError",
                version = "1.0",
                source = "let let: = ;",
                configJson = "{}",
            ),
        )
}
