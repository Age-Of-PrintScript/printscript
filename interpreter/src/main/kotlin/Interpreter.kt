import ast.Program

object Success;
object Failure;

//TODO -> Cambiar return horrible que pusimos
interface Interpreter {
    fun execute(program: Program): Either<Failure, Success>
}

class InterpreterImpl: Interpreter {
    override fun execute(program: Program): Either<Failure, Success> {
        TODO("Not yet implemented")
    }
}
