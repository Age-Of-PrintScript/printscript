package linter

import ast.AST
import kotlinx.serialization.json.JsonObject

interface LinterRule {
    fun apply(ast: AST): Warning?
}

interface LinterRuleFactory {
    val ruleName: String

    fun fromConfig(params: JsonObject): LinterRule
}
