package linter.rules

import ast.AST
import ast.Expression
import domain.Position
import kotlinx.serialization.json.JsonObject
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning
import tokens.Call
import java.util.Optional

// PrintlnArgumentRule.kt

class PrintlnArgumentRule : LinterRule {
    override fun apply(ast: AST): Optional<Warning> {
        if (ast !is Call || ast.type.name != "println") return Optional.empty()
        val arg = (ast as AST.Call).args.firstOrNull() ?: return Optional.empty()
        return if (arg is Expression.Variable || arg is Expression.Literal)
            Optional.empty()
        else
            Optional.of(Warning("println must be called with an identifier or literal", Position(0,0)))
    }
}

object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"
    override fun fromConfig(params: JsonObject): LinterRule = PrintlnArgumentRule()
}