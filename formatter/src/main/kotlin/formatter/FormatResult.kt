package formatter

sealed interface FormatResult<L, R>

data class FormatError<L, R>(
    val value: L,
) : FormatResult<L, R>

data class FormatSuccess<L, R>(
    val value: R,
) : FormatResult<L, R>

// Esta funcion esta explicada en los logs
inline fun <L, R> FormatResult<L, R>.getOrReturn(onFailure: (L) -> Nothing): R =
    when (this) {
        is FormatError -> onFailure(this.value)
        is FormatSuccess -> this.value
    }
