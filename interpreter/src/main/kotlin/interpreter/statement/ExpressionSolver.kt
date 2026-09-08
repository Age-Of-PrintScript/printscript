package interpreter.statement

import ast.Expression
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.LanguageSemantics
import interpreter.OperationKey

internal class ExpressionSolver {
    fun solve(
        expression: Expression,
        env: Map<String, Expression.Literal?>,
        semantics: LanguageSemantics,
    ): Either<String, Expression.Literal> {
        return when (expression) {
            is Expression.Call -> {
                val evaluatedArgs =
                    expression.args
                        .map { arg ->
                            solve(arg, env, semantics)
                                .getOrReturn { return Failure("") }
                                .toPSLiteral()
                        }

                val function = semantics.functions[expression.name] ?: return Failure("")
                val result = function.execute(evaluatedArgs).getOrReturn { return Failure("") }
                // si es una expression, el call deberia devolver algo, por eso el println no puede
                // ser igualado a una variable
                val literal = result.returnValue?.toLiteral() ?: return Failure("")
                Success(literal)
            }
            is Expression.Literal -> Success(expression)
            is Expression.Operation -> {
                val left = solve(expression.left, env, semantics).getOrReturn { return Failure("") }
                val right = solve(expression.right, env, semantics).getOrReturn { return Failure("") }
                val opKey = OperationKey(expression.operator, left.type, right.type)
                val operation = semantics.operations[opKey] ?: return Failure("")
                val result = operation.apply(left.toPSLiteral(), right.toPSLiteral()).getOrReturn { return Failure("") }
                Success(result.toLiteral())
            }
            is Expression.Variable -> {
                val value = env[expression.name] ?: return Failure("")
                Success(value)
            }
        }
    }
}
