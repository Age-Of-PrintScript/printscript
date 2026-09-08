package ast

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptType

sealed interface ASTViejo {
    data class Declaration(
        val id: ASTIdentifier,
        val type: ASTDataType,
        val value: ExpressionViejo?,
    ) : ASTViejo

    data class Assignment(
        val id: ASTIdentifier,
        val value: ExpressionViejo,
    ) : ASTViejo

    data class Call(
        val functionName: PrintScriptFunctions,
        val args: List<ExpressionViejo>,
    ) : ASTViejo
}

data class OldProgram(
    val trees: List<ASTViejo>,
    val start: Position,
    val end: Position,
)

data class ASTIdentifier(
    val name: String,
)

data class ASTDataType(
    val name: PrintScriptType,
)
