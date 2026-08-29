package formatter.formatrules


data class FormattingRules(val rules :List<FormatRule>)

interface FormatRule{
    fun apply(line: String): String
}

class LineBeforeCallRule(val lines: Int): FormatRule {

    override fun apply(line: String): String {
        val newlines = "\n".repeat(lines) // "".repeat(n: Int) te devuelve el string "" repetido n veces
        return newlines + line
    }
}

class SpaceBeforeColonRule(val ruleApplies: Boolean): FormatRule {
    override fun apply(line: String): String {
        if (ruleApplies) {
            return " $line"
        }
        return line
    }
}

class SpaceAfterColonRule(val ruleApplies: Boolean): FormatRule {
    override fun apply(line: String): String {
        if (ruleApplies) {
            return "$line "
        }
        return line
    }
}

class SpacesAroundAssignRule(val ruleApplies: Boolean): FormatRule {
    override fun apply(line: String): String {
        if(ruleApplies){
            return " $line "
        }
        return line
    }
}