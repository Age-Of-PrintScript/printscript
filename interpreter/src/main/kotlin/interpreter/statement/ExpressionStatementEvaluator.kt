package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment

class ExpressionStatementEvaluator : StatementEvaluator {
    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, RuntimeEnvironment> {
        val exprStatement = statement as AST.ExpressionStatement
        solveExpression(exprStatement.expression, env, io, semantics)
            .getOrReturn { return Failure(it) }
        return Success(env)
    }
}
