package linter

internal enum class IdentifierConvention(
    private val regex: Regex,
) {
    SNAKE_CASE(Regex("^[a-z][a-z0-9]*(_[a-z0-9]+)*$")),
    CAMEL_CASE(Regex("^[a-z][a-zA-Z0-9]*$")),
    ;

    fun matches(identifier: String): Boolean = regex.matches(identifier)

    companion object {
        fun from(value: String): IdentifierConvention? =
            when (value) {
                "camelCase" -> CAMEL_CASE
                "snake_case" -> SNAKE_CASE
                else -> null
            }
    }
}
