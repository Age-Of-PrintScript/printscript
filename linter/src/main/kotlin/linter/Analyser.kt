package linter

import ast.AST
import ast.Program

internal class Analyser {
    fun analyse(
        program: Program,
        rulesConfig: RulesConfig,
    ): List<Warning> = program.trees.flatMap { analyseNode(it, rulesConfig) }

    private fun analyseNode(
        ast: AST,
        rulesConfig: RulesConfig,
    ): List<Warning> {
        val currentWarnings = rulesConfig.rules.mapNotNull { it.apply(ast) }
        val nestedWarnings =
            when (ast) {
                is AST.ConditionalStatement -> {
                    ast.ifBlock.statements.flatMap { analyseNode(it, rulesConfig) } +
                        (ast.elseBlock?.statements?.flatMap { analyseNode(it, rulesConfig) } ?: emptyList())
                }
                else -> emptyList()
            }
        return currentWarnings + nestedWarnings
    }
}
