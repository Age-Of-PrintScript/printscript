package interpreter.statement

import ast.AST
import ast.Expression
import domain.Either
import domain.Failure
import domain.getOrReturn
import interpreter.CastKey
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
                if (solvedResult.type != declarationStatement.type) {
                    if (value is Expression.Call) {
                        val caster =
                            semantics
                                .typeCasters[CastKey(solvedResult.type, declarationStatement.type)]
                                ?: return Failure(RuntimeError.INVALID_CAST)

                        caster.cast(solvedResult).getOrReturn { return Failure(it) }
                    } else {
                        return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
                    }
                } else {
                    solvedResult
                }

            return updateEnvironmentWithNewDeclaration(
                env,
                declarationStatement.id,
                declarationStatement.type,
                finalValue.toLiteral(),
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
