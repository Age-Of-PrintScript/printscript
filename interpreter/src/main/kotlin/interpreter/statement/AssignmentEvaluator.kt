package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.getOrReturn
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment

class AssignmentEvaluator : StatementEvaluator {
    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, RuntimeEnvironment> {
        val assignmentStatement = statement as AST.AssignmentStatement
        val solvedValue =
            solveExpression(assignmentStatement.value, env, io, semantics)
                .getOrReturn { return Failure(it) }
                ?: return Failure(RuntimeError.MISSING_ASSIGNATION)

        return env.changeVariable(assignmentStatement.id, solvedValue.toLiteral())
    }
}
