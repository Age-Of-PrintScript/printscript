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

class AssignmentEvaluator : StatementEvaluator<AST.Assignment> {
    override fun evaluate(
        statement: AST.Assignment,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        val newValue =
            solveExpression(statement.value, env, semantics)
                .getOrReturn { return Failure(it) }

        if (newValue.returnValue == null) return Failure(RuntimeError.MISSING_ASSIGNATION)

        val newEnv =
            env
                .changeVariable(statement.id, newValue.returnValue.toLiteral())
                .getOrReturn { return Failure(it) }

        return Success(Pair(newEnv, events))
    }
}
