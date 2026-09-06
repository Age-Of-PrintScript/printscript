package linter

import ast.ASTViejo
import kotlinx.serialization.json.JsonObject

internal interface LinterRule {
    fun apply(astViejo: ASTViejo): Warning?
}

internal interface LinterRuleFactory {
    val ruleName: String

    fun fromConfig(params: JsonObject): LinterRule
}
