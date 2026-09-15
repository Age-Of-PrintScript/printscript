package formatter.formattokens

import ast.AST
import domain.Success

internal data class FormatTokenizers(
    val list: List<FormatTokenizer>,
) {
    fun find(ast: AST): FormatTokenizer? = list.firstOrNull { it.tokenize(ast) is Success }
}
