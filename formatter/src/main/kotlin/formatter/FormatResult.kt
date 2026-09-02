package formatter

import domain.Error

sealed interface FormatResult {
    data class Success(
        val output: String,
    ) : FormatResult

    data class Failure(
        val error: Error,
    ) : FormatResult
}
