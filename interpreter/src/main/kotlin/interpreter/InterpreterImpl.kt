package interpreter

import ast.Program
import domain.Either
import interpreter.environment.RuntimeEnvironment

internal class InterpreterImpl(
    private val semantics: LanguageSemantics,
) : Interpreter {
    override fun execute(
        program: Program,
        io: InterpreterIO,
        runtimeEnvironment: RuntimeEnvironment?,
    ): Either<RuntimeError, RuntimeEnvironment> =
        executeBlock(
            statements = program.trees,
            initialEnv = runtimeEnvironment ?: RuntimeEnvironment(),
            io = io,
            semantics = semantics,
        )
}
