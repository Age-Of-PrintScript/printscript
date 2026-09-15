package domain

interface Error {
    fun getMessage(): String

    val start: Position? get() = null
    val end: Position? get() = null
}
