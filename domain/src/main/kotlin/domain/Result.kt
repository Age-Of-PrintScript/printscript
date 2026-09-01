package domain

sealed interface Either<L, R>

data class Failure<L, R>(
    val value: L,
) : Either<L, R>

data class Success<L, R>(
    val value: R,
) : Either<L, R>

// Esta funcion esta explicada en los logs
inline fun <L, R> Either<L, R>.getOrReturn(onFailure: (L) -> Nothing): R =
    when (this) {
        is Failure -> onFailure(this.value)
        is Success -> this.value
    }
