package interpreter

import ast.AST
import ast.Program
import domain.Either
import domain.Failure
import domain.Success
import interpreter.environment.ExecutionResult
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.RuntimeEvents
import interpreter.statement.AssignmentEvaluator
import interpreter.statement.DeclarationEvaluator
import interpreter.statement.ExpressionStatementEvaluator

internal class InterpreterImpl(
    val semantics: LanguageSemantics,
) : Interpreter {
    private val declarationEvaluator = DeclarationEvaluator()
    private val assignmentEvaluator = AssignmentEvaluator()
    private val expressionStatementEvaluator = ExpressionStatementEvaluator()

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
            val result =
                when (ast) {
                    is AST.Declaration ->
                        declarationEvaluator.evaluate(ast, env, events, semantics)

                    is AST.Assignment ->
                        assignmentEvaluator.evaluate(ast, env, events, semantics)

                    is AST.ExpressionStatement ->
                        expressionStatementEvaluator.evaluate(ast, env, events, semantics)
                }

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
