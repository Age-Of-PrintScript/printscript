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

internal object ExpressionSolver {
    fun solve(
        expression: Expression,
        env: Map<String, PSLiteral?>,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<String, PSLiteral?> {
        return when (expression) {
            is Expression.Literal -> Success(PSLiteral(expression.value, expression.type))

            is Expression.Variable -> {
                val value = env[expression.name] ?: return Failure("")
                Success(value)
            }

            is Expression.Operation -> {
                val left =
                    solve(expression.left, env, io, semantics)
                        .getOrReturn { return Failure(it) }
                        ?: return Failure("Left operand has no value")

                val right =
                    solve(expression.right, env, io, semantics)
                        .getOrReturn { return Failure(it) }
                        ?: return Failure("Right operand has no value")

                val opKey = OperationKey(expression.operator, left.type, right.type)
                val operation = semantics.operations[opKey] ?: return Failure("Unsupported operation")
                val result = operation.apply(left, right).getOrReturn { return Failure(it.reason) }

                Success(result)
            }

            is Expression.Call -> {
                val evaluatedArgs = mutableListOf<PSLiteral>()
                for (arg in expression.args) {
                    val argVal =
                        solve(arg, env, io, semantics)
                            .getOrReturn { return Failure(it) }
                            ?: return Failure("Argument expression has no value")
                    evaluatedArgs.add(argVal)
                }

                val function = semantics.functions[expression.name] ?: return Failure("")
                val result =
                    function
                        .execute(evaluatedArgs.toList(), io)
                        .getOrReturn { return Failure("") }

                Success(result)
            }
        }
    }
}
