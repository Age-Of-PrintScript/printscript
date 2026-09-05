package linter

import ast.AST
import kotlinx.serialization.json.JsonObject

internal interface LinterRule {
    fun apply(ast: AST): Warning?
}

internal interface LinterRuleFactory {
    val ruleName: String

    fun fromConfig(params: JsonObject): LinterRule
}
