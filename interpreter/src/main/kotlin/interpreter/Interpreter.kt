package interpreter

import domain.Either
import ast.Program

//TODO -> Cambiar return horrible que pusimos
interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>
}

class InterpreterImpl: Interpreter {
    override fun execute(program: Program): Either<RuntimeError, ExecutionResult> {
        TODO("Not yet implemented")
    }
}
