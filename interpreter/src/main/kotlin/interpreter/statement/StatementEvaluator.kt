package interpreter.statement

import ast.AST
import domain.Either
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment
import interpreter.environment.RuntimeEvents

interface StatementEvaluator {
    fun canEvaluate(statement: AST): Boolean

    fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        events: RuntimeEvents,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, Pair<RuntimeEnvironment, RuntimeEvents>>
}
