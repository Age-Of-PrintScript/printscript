package formattest.cases

object FormatterFailureCases {
    fun cases(missingConfigPath: String): List<FormatterFailureCase> =
        listOf(
            FormatterFailureCase(
                name = "unknown printscript version returns FormatError",
                version = "9.9",
                source = "let x: number = 5;",
                configJson = "{}",
                expectedErrorMessage = "Unknown version",
            ),
            FormatterFailureCase(
                name = "non-existent config file returns FormatError",
                version = "1.0",
                source = "let x: number = 5;",
                configJson = null,
                customConfigPath = missingConfigPath,
                expectedErrorMessage = "Rules file could not be read",
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
