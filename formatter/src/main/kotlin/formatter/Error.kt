package formatter

internal sealed interface FormatterError {
    fun getMessage(): String
}

internal enum class FormattingError(
    val reason: String,
) : FormatterError {
    INVALID_JSON("Formatting rules are invalid"),
    UNKNOWN_AST_TYPE("Unknown Ast type of data"),
    ;

    override fun getMessage(): String = reason
}

internal data class FormatterConfigError(
    private val reason: String,
) : FormatterError {
    override fun getMessage(): String = reason
}
