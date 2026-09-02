package formatter

data class FormattingRules(
    val rulesList: List<FormatRule>,
)

interface FormatRule {
    fun apply(line: String): String
}

class LineBeforeCallRule(
    val lines: Int,
) : FormatRule {
    override fun apply(line: String): String {
        val newlines = "\n".repeat(lines) // "".repeat(n: Int) te devuelve el string "" repetido n veces
        return newlines + line
    }
}

class SpaceBeforeColonRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst(":", " :") else line
}

class SpaceAfterColonRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst(":", ": ") else line
}

class SpacesAroundAssignRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst("=", " = ") else line
}

class SemiColonAtTheEndRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) "$line;" else line
}
