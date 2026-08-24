package linter.rules

import ast.AST
import domain.Position
import linter.IdentifierConvention
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

class IdentifierFormatRule(val convention: IdentifierConvention): LinterRule {
    override fun apply(ast: AST): Warning? {
        val id = extractIdentifier(ast) ?: return null

        return if (convention.matches(id))
            null
        else
            Warning(
                "Identifier '$id' does not follow ${convention.name} convention",
                Position(0,0)
            )
    }

    private fun extractIdentifier(ast: AST): String? = when (ast) {
        is AST.Assignment -> ast.id.name
        is AST.Call -> null
        is AST.Declaration -> ast.id.name
    }

}

object IdentifierFormatRuleFactory : LinterRuleFactory {
    override val ruleName = "identifier-format"
    override fun fromConfig(params: JsonObject): LinterRule {
        val convention = params["convention"]?.jsonPrimitive?.content
            ?: throw RuntimeException("identifier-format rule requires 'convention' parameter")
        return IdentifierFormatRule(
            IdentifierConvention.from(convention)
                ?: throw RuntimeException("Unknown convention: $convention")
        )
    }
}