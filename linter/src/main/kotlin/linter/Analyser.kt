package linter

import ast.AST
import ast.Program

class Analyser {
    fun analyse(
        program: Program,
        rulesConfig: RulesConfig,
    ): List<Warning> = program.trees.flatMap { checkRules(it, rulesConfig) }

    fun checkRules(
        ast: AST,
        rulesConfig: RulesConfig,
    ): List<Warning> = rulesConfig.rules.mapNotNull { it.apply(ast) }
}
