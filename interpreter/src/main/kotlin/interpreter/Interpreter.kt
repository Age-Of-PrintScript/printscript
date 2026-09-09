package interpreter

import ast.Program
import domain.Either
import interpreter.environment.ExecutionResult
import interpreter.environment.RuntimeEnvironment

interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>

    fun executeWithEnvironment(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
    ): Either<RuntimeError, ExecutionResult>

    companion object {
        fun new(semantics: LanguageSemantics): Interpreter = InterpreterImpl(semantics)
    }
}
