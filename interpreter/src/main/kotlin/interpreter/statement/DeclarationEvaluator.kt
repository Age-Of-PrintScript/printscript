package interpreter.statement

import ast.AST
import domain.Either
import domain.Failure
import domain.getOrReturn
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment

class DeclarationEvaluator : StatementEvaluator {
    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, RuntimeEnvironment> {
        val declarationStatement = statement as AST.DeclarationStatement
        val value = declarationStatement.value

        if (value != null) {
            val solvedResult =
                solveExpression(value, env, io, semantics)
                    .getOrReturn { return Failure(it) }
                    ?: return Failure(RuntimeError.MISSING_ASSIGNATION)

            val finalValue =
                resolveWithCast(solvedResult, declarationStatement.type, value, semantics)
                    .getOrReturn { return Failure(it) }

            return updateEnvironmentWithNewDeclaration(
                env,
                declarationStatement.id,
                declarationStatement.type,
                finalValue,
                declarationStatement.mutable,
            )
        } else {
            return env.addVariable(
                declarationStatement.id,
                declarationStatement.type,
                null,
                declarationStatement.mutable,
            )
        }
    }
}
