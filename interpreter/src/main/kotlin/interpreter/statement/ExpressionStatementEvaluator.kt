package interpreter.statement

import ast.AST
import domain.Either
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
        TODO("Not yet implemented")
    }
}
