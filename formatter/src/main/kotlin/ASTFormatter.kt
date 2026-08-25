package formatter

import ast.AST

interface ASTFormatter {
    fun format(ast: AST): String
}