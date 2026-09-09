package interpreter

import ast.Program
import domain.Either
import domain.Failure
import domain.Success
import interpreter.environment.ExecutionResult
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.RuntimeEvents

internal class InterpreterImpl(
    val semantics: LanguageSemantics,
) : Interpreter {
    override fun execute(program: Program): Either<RuntimeError, ExecutionResult> = execute(program, RuntimeEnvironment(emptyMap()), RuntimeEvents(emptyList()))

    override fun executeWithEnvironment(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
    ): Either<RuntimeError, ExecutionResult> = execute(program, runtimeEnvironment, RuntimeEvents(emptyList()))

    private fun execute(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
        runtimeEvents: RuntimeEvents,
    ): Either<RuntimeError, ExecutionResult> {
        val asts = program.trees
        var events = runtimeEvents
        var env = runtimeEnvironment
        for (ast in asts) {
            val astType = ast.astType
            val evaluator =
                semantics.statementEvaluators[astType]
                    ?: return Failure(RuntimeError.MISSING_EVALUATOR_FOR_AST)

            val result = evaluator.evaluate(ast, env, events, semantics)

            when (result) {
                is Failure -> return Failure(result.value)
                is Success -> {
                    env = result.value.first
                    events = result.value.second
                }
            }
        }
        return Success(ExecutionResult(env, events))
    }
}
