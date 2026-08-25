package formatter.formatrules

class LineBeforeDeclarationRule(val lines: Int): FormatRule {

    override fun apply(line: String): String {
        val newlines = "\n".repeat(lines) // "".repeat(n: Int) te devuelve el string "" repetido n veces
        return newlines + line
    }
}