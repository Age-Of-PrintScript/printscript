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

        val expectedType =
            env.variableMap[assignmentStatement.id]?.type
                ?: return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)

        val finalValue =
            resolveWithCast(solvedValue, expectedType, assignmentStatement.value, semantics)
                .getOrReturn { return Failure(it) }

        return env.changeVariable(assignmentStatement.id, finalValue.toLiteral())
    }
}
