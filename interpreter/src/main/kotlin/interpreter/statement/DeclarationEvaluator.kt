package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.RuntimeEvents

class DeclarationEvaluator : StatementEvaluator<AST.Declaration> {
    override fun evaluate(
        statement: AST.Declaration,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        var currentEnv = env
        if (statement.value != null) {
            val solvedResult =
                solveExpression(statement.value!!, env, semantics)
                    .getOrReturn { return Failure(it) }

            if (solvedResult.returnValue == null) {
                return Failure(RuntimeError.MISSING_ASSIGNATION)
            }

            val newEnv =
                updateEnvironmentWithNewDeclaration(
                    env,
                    statement.id,
                    statement.type,
                    solvedResult.returnValue.toLiteral(),
                    statement.mutable,
                ).getOrReturn { return Failure(it) }

            currentEnv = newEnv
        } else {
            val newEnv =
                currentEnv
                    .addVariable(statement.id, statement.type, null, statement.mutable)
                    .getOrReturn { return Failure(it) }

            currentEnv = newEnv
        }
        return Success(Pair(currentEnv, events))
    }
}
