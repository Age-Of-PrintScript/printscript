package formatter.formatrules

import formatter.formattokens.EOL
import formatter.formattokens.FormatToken
import formatter.formattokens.FormatTokens
import formatter.formattokens.Indent
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

class EnsureSpaceAroundEquals(
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

class EnsureNoSpaceAroundEquals(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val list = tokens.list
        val newList = mutableListOf<FormatToken>()
        for ((i, token) in list.withIndex()) {
            // "?: token" cubre los bordes de la lista (sin vecino): al ser el propio WhiteSpace,
            // nunca matchea isText(_, "="), así que un espacio sin vecino no se considera "pegado al =".
            val isSpaceAroundEquals =
                token is WhiteSpace &&
                    (isText(list.getOrNull(i - 1) ?: token, "=") || isText(list.getOrNull(i + 1) ?: token, "="))
            if (!isSpaceAroundEquals) {
                newList.add(token)
            }
        }
        return FormatTokens(newList)
    }
}

class EnsureSpaceBeforeColon(
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

class EnsureSpaceAfterColon(
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

class LineBreaksAfterPrintLn(
    val lines: Number,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens =
        FormatTokens(
            splitIntoLines(tokens.list).flatMap { line ->
                if (isText(line.first(), "println")) {
                    line.dropLastWhile { it is EOL } + List(lines.toInt().coerceAtLeast(0)) { EOL }
                } else {
                    line
                }
            },
        )

    // Agrupa los tokens en "líneas": cada sentencia junto con todos los EOL consecutivos que la
    // siguen (líneas en blanco incluidas). Así, en una lista plana con varias sentencias, solo se
    // tocan los saltos de línea que le pertenecen a la sentencia que arranca con "println",
    // sin importar dónde esté ni qué más haya antes o después en la lista.

    private fun splitIntoLines(tokens: List<FormatToken>): List<List<FormatToken>> {
        val lines = mutableListOf<List<FormatToken>>()
        var current = mutableListOf<FormatToken>()
        for (token in tokens) {
            val startsNewLine = token !is EOL && current.lastOrNull() is EOL
            if (startsNewLine) {
                lines.add(current)
                current = mutableListOf()
            }
            current.add(token)
        }
        if (current.isNotEmpty()) lines.add(current)
        return lines
    }
}

// ---------------- 1.1 ---------------------

class IfBraceSameLine(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val newList = mutableListOf<FormatToken>()
        val list = tokens.list
        for ((i, token) in list.withIndex()) {
            if (isText(token, "{") && list.getOrNull(i - 1) is EOL) {
                if (newList.lastOrNull() is EOL) newList.removeAt(newList.lastIndex)
                newList.add(WhiteSpace)
            }
            newList.add(token)
        }
        return FormatTokens(newList)
    }
}

class IfBraceBelowLine(
    val activated: Boolean,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens {
        if (!activated) return tokens

        val newList = mutableListOf<FormatToken>()
        val list = tokens.list
        for ((i, token) in list.withIndex()) {
            if (isText(token, "{") && list.getOrNull(i - 1) !is EOL) {
                if (newList.lastOrNull() is WhiteSpace) newList.removeAt(newList.lastIndex)
                newList.add(EOL)
            }
            newList.add(token)
        }
        return FormatTokens(newList)
    }
}

class IndentsInsideIf(
    val indents: Int,
) : FormatRule {
    override fun apply(tokens: FormatTokens): FormatTokens =
        FormatTokens(
            tokens.list.flatMap { token ->
                if (token is Indent) List(indents) { WhiteSpace } else listOf(token)
            },
        )
}
