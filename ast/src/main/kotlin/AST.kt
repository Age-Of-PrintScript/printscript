interface AST {
    object Declaration : AST
    object Assignment : AST
    object Call : AST
}

//TODO -> Redefinir Declaration, Assignment y Call como hicimos en el pizarron

data class Program(val trees: List<AST>, val start: Position, val end: Position )
