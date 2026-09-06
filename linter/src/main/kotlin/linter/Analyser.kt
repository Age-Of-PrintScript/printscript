package linter

import ast.ASTViejo
import ast.Program

internal class Analyser {
    fun analyse(
        program: Program,
        rulesConfig: RulesConfig,
    ): List<Warning> = program.trees.flatMap { checkRules(it, rulesConfig) }

    fun checkRules(
        astViejo: ASTViejo,
        rulesConfig: RulesConfig,
    ): List<Warning> = rulesConfig.rules.mapNotNull { it.apply(astViejo) }
}
