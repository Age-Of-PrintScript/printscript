package linter.rules

import ast.AST
import ast.Expression
import domain.Position
import kotlinx.serialization.json.JsonObject
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

internal class PrintlnArgumentRule : LinterRule {
    override fun apply(ast: AST): Warning? {
        val call = extractPrintlnCall(ast) ?: return null
        val arg = call.args.firstOrNull() ?: return null

        return if (isValidArgument(arg)) {
            null
        } else {
            Warning(
                "println must be called with an identifier or literal",
                Position(0, 0),
            )
        }
    }

    private fun extractPrintlnCall(ast: AST): Expression.Call? {
        if (ast !is AST.ExpressionStatement) return null
        val expr = ast.expression
        if (expr !is Expression.Call || expr.name != "println") return null
        return expr
    }

    private fun isValidArgument(arg: Expression): Boolean = arg is Expression.Variable || arg is Expression.Literal
}

internal object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"

    override fun fromConfig(params: JsonObject): LinterRule = PrintlnArgumentRule()
}
