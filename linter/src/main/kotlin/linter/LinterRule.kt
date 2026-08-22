package linter

import ast.AST
import java.util.Optional

interface LinterRule {
    fun apply(ast: AST): Optional<Warning>
}