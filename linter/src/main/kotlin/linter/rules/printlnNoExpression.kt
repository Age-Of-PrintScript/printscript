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
import java.util.Optional


class PrintlnArgumentRule : LinterRule {
    override fun apply(ast: AST): Optional<Warning> {
        if (notAPrintCall(ast)) return Optional.empty()
        val ast = ast as Call
        val arg = ast.args.firstOrNull() ?: return Optional.empty()
        return if (argIsNotExpression(arg))
            Optional.empty()
        else
            Optional.of(Warning(
            "println must be called with an identifier or literal",
                Position(0,0)
            ))
    }
    private fun argIsNotExpression(arg: Expression) =
        arg is Expression.Variable || arg is Expression.Literal

    private fun notAPrintCall(ast: AST)=
        ast !is Call || ast.functionName != PrintScriptFunctions.PRINTLN
}

object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"
    override fun fromConfig(params: JsonObject): LinterRule = PrintlnArgumentRule()
}