package interpreter.statement

import ast.Expression
import domain.Either
import domain.Failure
import domain.PSLiteral
import domain.Success
import domain.getOrReturn
import interpreter.LanguageSemantics
import interpreter.OperationKey
import interpreter.environment.Event

internal class ExpressionSolver {
    fun solve(
        expression: Expression,
        env: Map<String, Expression.Literal?>,
        semantics: LanguageSemantics,
    ): Either<String, ExpressionResult> {
        return when (expression) {
            is Expression.Literal -> Success(ExpressionResult(returnValue = expression.toPSLiteral()))

            is Expression.Variable -> {
                val value = env[expression.name] ?: return Failure("")
                Success(ExpressionResult(returnValue = value.toPSLiteral()))
            }

            is Expression.Operation -> {
                val leftRes = solve(expression.left, env, semantics).getOrReturn { return Failure(it) }
                val leftVal = leftRes.returnValue ?: return Failure("Left operand has no value")

                val rightRes = solve(expression.right, env, semantics).getOrReturn { return Failure(it) }
                val rightVal = rightRes.returnValue ?: return Failure("Right operand has no value")

                val opKey = OperationKey(expression.operator, leftVal.type, rightVal.type)
                val operation = semantics.operations[opKey] ?: return Failure("Unsupported operation")
                val result = operation.apply(leftVal, rightVal).getOrReturn { return Failure(it.reason) }

                Success(
                    ExpressionResult(
                        returnValue = result,
                        events = leftRes.events + rightRes.events,
                    ),
                )
            }

            is Expression.Call -> {
                val evaluatedArgs = mutableListOf<PSLiteral>()
                val argEvents = mutableListOf<Event>()

                for (arg in expression.args) {
                    val argRes =
                        solve(arg, env, semantics)
                            .getOrReturn { return Failure(it) }

                    val argVal =
                        argRes.returnValue
                            ?: return Failure("Argument expression has no value")

                    evaluatedArgs.add(argVal)
                    argEvents.addAll(argRes.events)
                }

                val function = semantics.functions[expression.name] ?: return Failure("")
                val result = function.execute(evaluatedArgs).getOrReturn { return Failure("") }

                Success(
                    ExpressionResult(
                        returnValue = result.returnValue,
                        events = argEvents + result.events,
                    ),
                )
            }
        }
    }
}
