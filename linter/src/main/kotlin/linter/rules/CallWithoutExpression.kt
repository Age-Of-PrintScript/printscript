package linter.rules

import ast.AST
import ast.Expression
import domain.Position
import kotlinx.serialization.json.JsonObject
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

internal data class CallArgumentRule(
    val name: String,
) : LinterRule {
    override fun apply(ast: AST): Warning? {
        val arg = getCallArguments(ast, name) ?: return null

        return if (isValidArgument(arg)) {
            null
        } else {
            Warning(
                "$name must be called with an identifier or literal",
                Position(0, 0),
            )
        }
    }

    private fun getCallArguments(
        ast: AST,
        functionName: String,
    ): Expression? {
        val call = extractCall(ast, functionName) ?: return null
        return call.args.firstOrNull()
    }

    private fun extractCall(
        ast: AST,
        functionName: String,
    ): Expression.Call? {
        if (ast !is AST.ExpressionStatement) return null
        val expr = ast.expression
        if (expr !is Expression.Call || expr.name != functionName) return null
        return expr
    }

    private fun isValidArgument(arg: Expression): Boolean = arg is Expression.Variable || arg is Expression.Literal
}

internal object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"

    override fun fromConfig(params: JsonObject): LinterRule = CallArgumentRule("println")
}

internal object ReadInputArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "readInput-no-expression"

    override fun fromConfig(params: JsonObject): LinterRule = CallArgumentRule("readInput")
}
