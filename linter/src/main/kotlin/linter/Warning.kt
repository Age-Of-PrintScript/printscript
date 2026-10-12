package linter

import domain.Error
import domain.Position

data class LinterPosition(
    val line: Int,
    val charPosition: Int,
) {
    companion object {
        internal fun fromEnginePosition(position: Position): LinterPosition = LinterPosition(position.line, position.charPosition)
    }
}

data class Warning(
    val message: String,
    val position: LinterPosition,
) {
    override fun toString(): String = "[$position] $message"

    companion object {
        internal fun fromError(error: Error): Warning =
            Warning(
                error.getMessage(),
                LinterPosition.fromEnginePosition(error.start ?: Position.START),
            )
    }
}
