package formatter

internal data class FormattingRules(
    val rulesList: List<FormatRule>,
)

internal interface FormatRule {
    fun apply(line: String): String
}

internal class LineBeforeCallRule(
    val lines: Int,
) : FormatRule {
    override fun apply(line: String): String {
        val newlines = "\n".repeat(lines) // "".repeat(n: Int) te devuelve el string "" repetido n veces
        return newlines + line
    }
}

internal class SpaceBeforeColonRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst(":", " :") else line
}

internal class SpaceAfterColonRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst(":", ": ") else line
}

internal class SpacesAroundAssignRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) line.replaceFirst("=", " = ") else line
}

internal class SemiColonAtTheEndRule(
    val ruleApplies: Boolean,
) : FormatRule {
    override fun apply(line: String): String = if (ruleApplies) "$line;" else line
}
