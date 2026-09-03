package formatter

sealed interface FormatResult {
    data class Success(
        val output: String,
    ) : FormatResult

    data class Failure(
        val error: String,
    ) : FormatResult
}
