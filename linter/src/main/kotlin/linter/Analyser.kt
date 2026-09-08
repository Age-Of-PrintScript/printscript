package linter

import ast.ASTViejo
import ast.OldProgram

internal class Analyser {
    fun analyse(
        oldProgram: OldProgram,
        rulesConfig: RulesConfig,
    ): List<Warning> = oldProgram.trees.flatMap { checkRules(it, rulesConfig) }

    fun checkRules(
        astViejo: ASTViejo,
        rulesConfig: RulesConfig,
    ): List<Warning> = rulesConfig.rules.mapNotNull { it.apply(astViejo) }
}
