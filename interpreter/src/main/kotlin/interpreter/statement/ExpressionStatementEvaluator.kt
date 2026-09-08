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

class ExpressionStatementEvaluator : StatementEvaluator<AST.ExpressionStatement> {
    override fun evaluate(
        statement: AST.ExpressionStatement,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>> {
        val result =
            solveExpression(statement.expression, env, semantics)
                .getOrReturn { return Failure(it) }
        val updatedEvents = events + result.events
        return Success(Pair(env, updatedEvents))
    }
}
