package formatter.formatrules

import formatter.formattokens.EOL
import formatter.formattokens.FormatToken
import formatter.formattokens.FormatTokens
import formatter.formattokens.Text
import formatter.formattokens.WhiteSpace

interface FormatRule { // Las reglas tienen que ir en orden, sino se rompe. Ojo cuando armen la config
    fun apply(tokens: FormatTokens): FormatTokens
}

private fun isText(
    token: FormatToken,
    value: String,
) = token is Text && token.value == value

data class FormatRules(
    val list: List<FormatRule>,
)

class SpaceAroundAssign(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val newList = mutableListOf<FormatToken>()
        val list = tokens.list
        for ((i, token) in list.withIndex()) {
            if (isText(token, "=") && list.getOrNull(i - 1) !is WhiteSpace) {
                newList.add(WhiteSpace)
            }
            newList.add(token)
            if (isText(token, "=") && list.getOrNull(i + 1) !is WhiteSpace) {
                newList.add(WhiteSpace)
            }
        }
        return FormatTokens(newList)
    }
}

class SpaceBeforeColon(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val newList = mutableListOf<FormatToken>()
        val list = tokens.list
        for ((i, token) in list.withIndex()) {
            if (isText(token, ":") && list.getOrNull(i - 1) !is WhiteSpace) {
                newList.add(WhiteSpace)
            }
            newList.add(token)
        }
        return FormatTokens(newList)
    }
}

class SpaceAfterColon(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val newList = mutableListOf<FormatToken>()
        val list = tokens.list
        for ((i, token) in list.withIndex()) {
            newList.add(token)
            if (isText(token, ":") && list.getOrNull(i + 1) !is WhiteSpace) {
                newList.add(WhiteSpace)
            }
        }
        return FormatTokens(newList)
    }
}

class LinesAfterCall(
    val lines: Number,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        val withoutTrailingEol = tokens.list.dropLastWhile { it is EOL }
        val result = withoutTrailingEol + List(lines.toInt().coerceAtLeast(0)) { EOL }
        return FormatTokens(result)
    }
}
