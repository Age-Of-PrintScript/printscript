package formatter.formattokens

internal sealed interface FormatToken

internal object WhiteSpace : FormatToken // ' '

internal object EOL : FormatToken // '/n'

internal object Indent : FormatToken // '/t'

internal data class Text(
    val value: String,
) : FormatToken

internal data class FormatTokens(
    val list: List<FormatToken>,
) {
    fun add(token: FormatToken): FormatTokens {
        val newList = list.toMutableList()
        newList.add(token)
        return FormatTokens(newList)
    }

    fun remove(token: FormatToken): FormatTokens {
        val newList = list.toMutableList()
        newList.remove(token)
        return FormatTokens(newList)
    }
}
