package formattest.cases

data class FormatterSuccessCase(
    val name: String,
    val version: String,
    val source: String,
    val configJson: String,
    val expectedOutput: String,
)

data class FormatterFailureCase(
    val name: String,
    val version: String,
    val source: String,
    val configJson: String? = "{}",
    val customConfigPath: String? = null,
    val expectedErrorMessage: String? = null,
)
