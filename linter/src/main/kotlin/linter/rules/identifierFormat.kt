package linter.rules

import ast.AST
import domain.Position
import linter.IdentifierConvention
import linter.LinterRule
import linter.Warning
import java.util.Optional

class IdentifierFormatRule(val convention: IdentifierConvention): LinterRule {
    override fun apply(ast: AST): Optional<Warning> {
        val id = extractIdentifier(ast) ?: return Optional.empty()

        return if (convention.matches(id))
            Optional.empty()
        else
            Optional.of(
                Warning(
                    "Identifier '$id' does not follow ${convention.name} convention",
                    Position(0,0)
                )
            )
    }

    private fun extractIdentifier(ast: AST): String? = when (ast) {
        is AST.Assignment -> ast.id.name
        is AST.Call -> null
        is AST.Declaration -> ast.id.name
    }

}