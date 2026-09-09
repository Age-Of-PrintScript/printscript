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

class DeclarationEvaluator : StatementEvaluator {
    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        val declarationStatement = statement as AST.DeclarationStatement
        var currentEnv = env
        var newEvents = events
        val value = declarationStatement.value
        if (value != null) {
            val solvedResult =
                solveExpression(value, env, semantics)
                    .getOrReturn { return Failure(it) }

            newEvents += solvedResult.events

            if (solvedResult.returnValue == null) {
                return Failure(RuntimeError.MISSING_ASSIGNATION)
            }

            val newEnv =
                updateEnvironmentWithNewDeclaration(
                    env,
                    declarationStatement.id,
                    declarationStatement.type,
                    solvedResult.returnValue.toLiteral(),
                    declarationStatement.mutable,
                ).getOrReturn { return Failure(it) }

            currentEnv = newEnv
        } else {
            val newEnv =
                currentEnv
                    .addVariable(declarationStatement.id, declarationStatement.type, null, declarationStatement.mutable)
                    .getOrReturn { return Failure(it) }

            currentEnv = newEnv
        }
        return Success(Pair(currentEnv, newEvents))
    }
}
