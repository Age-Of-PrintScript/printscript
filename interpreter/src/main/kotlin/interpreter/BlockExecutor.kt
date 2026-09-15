package interpreter

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.environment.RuntimeEnvironment

internal fun executeBlock(
    statements: List<AST>,
    initialEnv: RuntimeEnvironment,
    io: InterpreterIO,
    semantics: LanguageSemantics,
): Either<RuntimeError, RuntimeEnvironment> {
    var env = initialEnv
    for (ast in statements) {
        val evaluator =
            semantics.statementEvaluators[ast.astType]
                ?: return Failure(RuntimeError.MISSING_EVALUATOR_FOR_AST.withPosition(ast.start, ast.end))
        env =
            evaluator.evaluate(ast, env, io, semantics).getOrReturn {
                val errorWithPos = if (it.start == null) it.withPosition(ast.start, ast.end) else it
                return Failure(errorWithPos)
            }
    }
    return Success(env)
}
