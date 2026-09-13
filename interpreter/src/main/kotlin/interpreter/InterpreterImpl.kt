package interpreter

import ast.Program
import domain.Either
import domain.Failure
import domain.Success
import interpreter.environment.RuntimeEnvironment

internal class InterpreterImpl(
    val semantics: LanguageSemantics,
) : Interpreter {
    override fun execute(
        program: Program,
        io: InterpreterIO,
        runtimeEnvironment: RuntimeEnvironment?,
    ): Either<RuntimeError, RuntimeEnvironment> {
        var env = runtimeEnvironment ?: RuntimeEnvironment(emptyMap())
        for (ast in program.trees) {
            val astType = ast.astType
            val evaluator =
                semantics.statementEvaluators[astType]
                    ?: return Failure(RuntimeError.MISSING_EVALUATOR_FOR_AST)

            val result = evaluator.evaluate(ast, env, io, semantics)

            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> env = result.value
            }
        }
        return Success(env)
    }
}
