package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.RuntimeEvents

class AssignmentEvaluator : StatementEvaluator<AST.Assignment> {
    override fun evaluate(
        statement: AST.Assignment,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        var currentEnv = env
        when (val newValue = solveExpression(statement.value, env)) {
            is Failure -> return Failure(newValue.value)
            is Success -> {
                when (val changedEnvResult = env.changeVariable(statement.id, newValue.value)) {
                    is Failure -> return Failure(changedEnvResult.value)
                    is Success -> {
                        currentEnv = changedEnvResult.value
                    }
                }
            }
        }
        return Success(Pair(currentEnv, events))
    }
}
