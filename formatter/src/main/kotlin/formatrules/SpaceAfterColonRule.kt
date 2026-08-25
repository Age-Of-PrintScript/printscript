package formatter.formatrules

class SpaceAfterColonRule(val ruleApplies: Boolean): FormatRule {
    override fun apply(line: String): String {
        if (ruleApplies) {
            return "$line "
        }
        return line
    }
}