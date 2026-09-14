package formatter.formattokens

sealed interface FormatToken

object WhiteSpace : FormatToken // ' '

object EOL : FormatToken // '/n'

object Indent : FormatToken // '/t'

data class Text(
    val value: String,
) : FormatToken

data class FormatTokens(
    val list: List<FormatToken>,
)
