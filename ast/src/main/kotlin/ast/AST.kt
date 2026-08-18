package ast

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptType

sealed interface AST {
    data class Declaration(val id: ASTIdentifier, val type: ASTDataType, val value: Expression?) : AST
    data class Assignment(val id: ASTIdentifier, val value: Expression) : AST
    data class Call(val functionName: PrintScriptFunctions, val args: List<Expression>) : AST
}

data class Program(val trees: List<AST>, val start: Position, val end: Position)

data class ASTIdentifier(val name: String)
data class ASTDataType(val name: PrintScriptType)