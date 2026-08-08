interface AST {
    data class Declaration(val id: Identifier, val type: DataType, val value: Expression?) : AST
    data class Assignment(val id: Identifier, val value: Expression) : AST
    data class Call(val functionName: String) : AST
}

//TODO -> Redefinir Declaration, Assignment y Call como hicimos en el pizarron

data class Program(val trees: List<AST>, val start: Position, val end: Position )

data class Identifier(val name: String)
data class DataType(val name: String)