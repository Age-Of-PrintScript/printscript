package formatter

import ast.AST

internal interface ASTFormatter {
    fun format(ast: AST): String
}

internal class FormatterImplementation(
    val rules: FormattingRules,
) : ASTFormatter {
    override fun format(ast: AST): String {
        val astToString = astToString(ast)
        var result = astToString

        for (rule in rules.rulesList) {
            result = rule.apply(result)
        }
        return result
    }
}
