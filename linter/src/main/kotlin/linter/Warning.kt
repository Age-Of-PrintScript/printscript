package linter

import domain.Error
import domain.Position

data class Warning(
    val message: String,
    val position: Position,
) {
    override fun toString(): String = "[$position] $message"

    companion object {
        fun fromError(error: Error): Warning =
            Warning(
                error.getMessage(),
                error.start ?: Position.START,
            )
    }
}
