package linter.rules

import ast.AST
import ast.AST.Call
import ast.Expression
import domain.Position
import domain.PrintScriptFunctions
import kotlinx.serialization.json.JsonObject
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

internal class PrintlnArgumentRule : LinterRule {
    override fun apply(ast: AST): Warning? {
        if (notAPrintCall(ast)) return null
        val astCall = ast as Call
        val arg = astCall.args.firstOrNull() ?: return null
        return if (argIsNotExpression(arg)) {
            null
        } else {
            Warning(
                "println must be called with an identifier or literal",
                Position(0, 0),
            )
        }
    }

    private fun argIsNotExpression(arg: Expression) = arg is Expression.Variable || arg is Expression.Literal

    private fun notAPrintCall(ast: AST) = ast !is Call || ast.functionName != PrintScriptFunctions.PRINTLN
}

internal object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"

    override fun fromConfig(params: JsonObject): LinterRule = PrintlnArgumentRule()
}
