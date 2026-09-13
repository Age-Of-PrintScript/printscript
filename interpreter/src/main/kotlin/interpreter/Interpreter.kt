package interpreter

import ast.Program
import domain.Either
import interpreter.environment.RuntimeEnvironment

interface Interpreter {
    fun execute(
        program: Program,
        io: InterpreterIO,
        runtimeEnvironment: RuntimeEnvironment? = null,
    ): Either<RuntimeError, RuntimeEnvironment>

    companion object {
        fun new(semantics: LanguageSemantics): Interpreter = InterpreterImpl(semantics)
    }
}
