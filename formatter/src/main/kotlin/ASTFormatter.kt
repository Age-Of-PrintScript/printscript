import ast.AST

interface ASTFormatter {
    fun format(ast: AST): String
}

class FormatterImplementation(
    val rules: FormattingRules,
) : ASTFormatter {
    override fun format(ast: AST): String {
        val astToString = astToString(ast)
        var result = astToString

        for (rule in rules.rules) {
            result = rule.apply(astToString)
        }
        return result
    }
}
