package formatter

import ast.ASTViejo

internal interface ASTFormatter {
    fun format(astViejo: ASTViejo): String
}

internal class FormatterImplementation(
    val rules: FormattingRules,
) : ASTFormatter {
    override fun format(astViejo: ASTViejo): String {
        val astToString = astToString(astViejo)
        var result = astToString

        for (rule in rules.rulesList) {
            result = rule.apply(result)
        }
        return result
    }
}
