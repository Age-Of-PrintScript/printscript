package interpreter.statement

import ast.Expression
import domain.Either
import domain.Failure
import domain.PSLiteral
import domain.Success
import domain.getOrReturn
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.OperationKey
import interpreter.RuntimeError

internal object ExpressionSolver {
    fun solve(
        expression: Expression,
        env: Map<String, PSLiteral?>,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, PSLiteral?> =
        when (expression) {
            is Expression.Literal -> Success(PSLiteral(expression.value, expression.type))

            is Expression.Variable -> {
                if (!env.containsKey(expression.name)) {
                    Failure(RuntimeError.VARIABLE_DOESNT_EXIST)
                } else {
                    val value = env[expression.name] ?: return Failure(RuntimeError.VARIABLE_NOT_INITIALIZED)
                    Success(value)
                }
            }

            is Expression.Operation -> {
                val left =
                    solve(expression.left, env, io, semantics)
                        .getOrReturn { return Failure(it) }
                        ?: return Failure(RuntimeError.VARIABLE_NOT_INITIALIZED)

                val right =
                    solve(expression.right, env, io, semantics)
                        .getOrReturn { return Failure(it) }
                        ?: return Failure(RuntimeError.VARIABLE_NOT_INITIALIZED)

                val opKey = OperationKey(expression.operator, left.type, right.type)
                val operation = semantics.operations[opKey] ?: return Failure(RuntimeError.UNSUPPORTED_OPERATION)
                val result = operation.apply(left, right).getOrReturn { return Failure(it) }

                Success(result)
            }

            is Expression.Call -> {
                val evaluatedArgs = mutableListOf<PSLiteral>()
                for (arg in expression.args) {
                    val argVal =
                        solve(arg, env, io, semantics)
                            .getOrReturn { return Failure(it) }
                            ?: return Failure(RuntimeError.VARIABLE_NOT_INITIALIZED)
                    evaluatedArgs.add(argVal)
                }

                val function = semantics.functions[expression.name] ?: return Failure(RuntimeError.FUNCTION_NOT_FOUND)
                val result =
                    function
                        .execute(evaluatedArgs.toList(), io)
                        .getOrReturn { return Failure(it) }

                Success(result)
            }
        }
}
