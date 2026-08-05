interface AST {
    object Declaration : AST
    object Assignment : AST
    object Call : AST
}

data class Program(val trees: List<AST>, val start: Position, val end: Position )
