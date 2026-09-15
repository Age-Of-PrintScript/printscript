package interpreter

import ast.AST
import ast.Program
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.environment.RuntimeEnvironment

internal class InterpreterImpl(
    private val semantics: LanguageSemantics,
) : Interpreter {
    override fun execute(
        program: Program,
        io: InterpreterIO,
        runtimeEnvironment: RuntimeEnvironment?,
    ): Either<RuntimeError, RuntimeEnvironment> {
        var env = runtimeEnvironment ?: RuntimeEnvironment()
        for (statement in program.trees) {
            env = executeStatement(statement, io, env).getOrReturn { return Failure(it) }
        }
        return Success(env)
    }

    override fun executeStatement(
        statement: AST,
        io: InterpreterIO,
        runtimeEnvironment: RuntimeEnvironment?,
    ): Either<RuntimeError, RuntimeEnvironment> {
        val env = runtimeEnvironment ?: RuntimeEnvironment()
        val evaluator =
            semantics.statementEvaluators[statement.astType]
                ?: return Failure(RuntimeError.MISSING_EVALUATOR_FOR_AST)
        return evaluator.evaluate(statement, env, io, semantics)
    }
}
