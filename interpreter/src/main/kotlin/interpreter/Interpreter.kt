package interpreter

import Either
import ast.Program

//TODO -> Cambiar return horrible que pusimos
interface Interpreter {
    fun execute(program: Program): Either<Unit, Unit>
}

class InterpreterImpl: Interpreter {
    override fun execute(program: Program): Either<Unit, Unit> {
        TODO("Not yet implemented")
    }
}
