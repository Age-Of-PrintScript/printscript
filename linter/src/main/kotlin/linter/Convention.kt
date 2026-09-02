package linter

internal enum class IdentifierConvention {
    SNAKE_CASE,
    CAMEL_CASE,
    ;

    fun matches(identifier: String): Boolean =
        when (this) {
            CAMEL_CASE -> Regex("^[a-z][a-zA-Z0-9]*$").matches(identifier)
            SNAKE_CASE -> Regex("^[a-z][a-z0-9]*(_[a-z0-9]+)*$").matches(identifier)
        }

    companion object {
        fun from(value: String): IdentifierConvention? =
            when (value) {
                "camelCase" -> CAMEL_CASE
                "snake_case" -> SNAKE_CASE
                else -> null
            }
    }
}
