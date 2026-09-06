package linter.rules

import ast.ASTViejo
import domain.Position
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import linter.IdentifierConvention
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

internal class IdentifierFormatRule(
    val convention: IdentifierConvention,
) : LinterRule {
    override fun apply(astViejo: ASTViejo): Warning? {
        val id = extractIdentifier(astViejo) ?: return null

        return if (convention.matches(id)) {
            null
        } else {
            Warning(
                "Identifier '$id' does not follow ${convention.name} convention",
                Position(0, 0),
            )
        }
    }

    private fun extractIdentifier(astViejo: ASTViejo): String? =
        when (astViejo) {
            is ASTViejo.Assignment -> astViejo.id.name
            is ASTViejo.Call -> null
            is ASTViejo.Declaration -> astViejo.id.name
        }
}

internal object IdentifierFormatRuleFactory : LinterRuleFactory {
    override val ruleName = "identifier-format"

    override fun fromConfig(params: JsonObject): LinterRule {
        val convention =
            params["convention"]?.jsonPrimitive?.content
                ?: throw IllegalArgumentException("identifier-format rule requires 'convention' parameter")
        return IdentifierFormatRule(
            IdentifierConvention.from(convention)
                ?: throw IllegalArgumentException("Unknown convention: $convention"),
        )
    }
}
