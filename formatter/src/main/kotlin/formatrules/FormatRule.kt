package formatter.formatrules


data class FormattingRules(val rules :List<FormatRule>)

interface FormatRule{
    fun apply(line: String): String
}

