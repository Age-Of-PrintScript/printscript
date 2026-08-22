package linter

import ast.AST
import kotlinx.serialization.json.JsonObject
import java.util.Optional

interface LinterRule {
    fun apply(ast: AST): Optional<Warning>
}

interface LinterRuleFactory {
    val ruleName: String
    fun fromConfig(json: JsonObject): LinterRule
}