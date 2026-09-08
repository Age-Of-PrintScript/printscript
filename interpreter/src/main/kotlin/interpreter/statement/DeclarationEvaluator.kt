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
    override fun canEvaluate(statement: AST): Boolean = statement is AST.Declaration

    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        val declaration = statement as AST.Declaration
        var currentEnv = env
        val value = declaration.value
        if (value != null) {
            val solvedResult =
                solveExpression(value, env, semantics)
                    .getOrReturn { return Failure(it) }

            if (solvedResult.returnValue == null) {
                return Failure(RuntimeError.MISSING_ASSIGNATION)
            }

            val newEnv =
                updateEnvironmentWithNewDeclaration(
                    env,
                    declaration.id,
                    declaration.type,
                    solvedResult.returnValue.toLiteral(),
                    declaration.mutable,
                ).getOrReturn { return Failure(it) }

            currentEnv = newEnv
        } else {
            val newEnv =
                currentEnv
                    .addVariable(declaration.id, declaration.type, null, declaration.mutable)
                    .getOrReturn { return Failure(it) }

            currentEnv = newEnv
        }
        return Success(Pair(currentEnv, events))
    }
}
