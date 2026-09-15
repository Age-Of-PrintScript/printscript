package domain

data class Position(
    val line: Int,
    val charPosition: Int,
) {
    override fun toString(): String = "$line:$charPosition"

    companion object {
        val START = Position(1, 1)
        val UNKNOWN = Position(0, 0)
    }
}
