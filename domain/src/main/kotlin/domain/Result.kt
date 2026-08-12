package domain

sealed interface Either<L, R>

data class Failure<L, R>(val value: L) : Either<L, R>
data class Success<L, R>(val value: R) : Either<L, R>