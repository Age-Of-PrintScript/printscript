package formatter

import ast.AST
import formatter.formatrules.FormattingRules

class FormatterImplementation(val rules: FormattingRules): ASTFormatter {
    override fun format(ast: AST): String {
        val astToString = astToString(ast)
        var result = ""

        for (rule in rules.rules) {
            result = rule.apply(astToString)
        }
        return result
    }
}
