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
        val calls = extractCalls(ast, name)
        for (call in calls) {
            val arg = call.args.firstOrNull() ?: continue
            if (!isValidArgument(arg)) {
                return Warning(
                    "$name must be called with an identifier or literal",
                    Position(0, 0),
                )
            }
        }
        return null
    }

    private fun extractCalls(
        ast: AST,
        functionName: String,
    ): List<Expression.Call> =
        when (ast) {
            is AST.ExpressionStatement -> findCalls(ast.expression, functionName)
            is AST.DeclarationStatement -> ast.value?.let { findCalls(it, functionName) } ?: emptyList()
            is AST.AssignmentStatement -> findCalls(ast.value, functionName)
            is AST.ConditionalStatement -> findCalls(ast.condition, functionName)
        }

    private fun findCalls(
        expr: Expression,
        functionName: String,
    ): List<Expression.Call> =
        when (expr) {
            is Expression.Call -> {
                val matches = if (expr.name == functionName) listOf(expr) else emptyList()
                matches + expr.args.flatMap { findCalls(it, functionName) }
            }
            is Expression.Operation -> findCalls(expr.left, functionName) + findCalls(expr.right, functionName)
            is Expression.Literal, is Expression.Variable -> emptyList()
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
