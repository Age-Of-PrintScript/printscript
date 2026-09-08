package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
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
            when (val solvedResult = solveExpression(statement.value!!, env, semantics)) {
                is Failure -> return Failure(solvedResult.value)
                is Success -> {
                    val newEnv =
                        updateEnvironmentWithNewDeclaration(env, statement.id, statement.type, solvedResult.value) // edito el environment
                    when (newEnv) {
                        is Failure -> return Failure(newEnv.value)
                        is Success -> {
                            currentEnv = newEnv.value
                        }
                    }
                }
            }
        } else {
            when (val newEnv = currentEnv.addVariable(statement.id, statement.type, null)) {
                is Failure -> return Failure(newEnv.value)
                is Success -> currentEnv = newEnv.value
            }
        }
        return Success(Pair(currentEnv, events))
    }
}
