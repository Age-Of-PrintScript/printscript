package ast

import domain.Either
import domain.Failure
import domain.Success
import domain.PrintScriptOperator
import domain.PrintScriptValue.NumberLiteral
import domain.PrintScriptValue.StringLiteral
import domain.PrintScriptValue
import java.util.Optional
import ast.Expression.Literal
import ast.Expression.Operation

class ExpressionSolver {


    fun solve(
        expression: Expression,
        values: Map<String, Optional<PrintScriptValue>>
    ): Either<String, PrintScriptValue> {

        return when (expression) {
            is Literal -> {
                when (expression.value) {
                    is NumberLiteral -> Success(expression.value)
                    is StringLiteral -> Success(expression.value)
                }
            }

            is Expression.Variable -> {
                val optionalValue = values[expression.name]
                    ?: return Failure("variable is not defined")

                if (optionalValue.isPresent) {
                    Success(optionalValue.get())
                } else {
                    Failure("variable is not initialized")
                }
            }

            is Operation -> solveOperation(expression, values)
        }
    }


    private fun solveOperation(
        operation: Operation,
        values: Map<String, Optional<PrintScriptValue>>
    ):
            Either<String, PrintScriptValue> {

        val left = solve(operation.left, values)
        val right = solve(operation.right, values)

        if (left is Failure) return Failure(left.value)
        if (right is Failure) return Failure(right.value)

        if (left is Success && right is Success) {

            if (stringAndNumberInOperation(left.value, right.value)) {
                return when (operation.operator) {
                    PrintScriptOperator.SUM -> {
                        handleStringAndNumberSum(left.value, right.value)
                    }

                    PrintScriptOperator.SUBTRACT -> {
                        Failure("cannot handle subtraction with different types")
                    }

                    PrintScriptOperator.MULTIPLY -> {
                        Failure("cannot handle multiplication with different types")
                    }

                    PrintScriptOperator.DIVIDE -> {
                        Failure("cannot handle division with different types")
                    }
                }
            }

            return when (operation.operator) {
                PrintScriptOperator.SUM -> {
                    dispatchSumOperation(left.value, right.value)
                }

                PrintScriptOperator.SUBTRACT -> {
                    dispatchSubtractOperation(left.value, right.value)
                }

                PrintScriptOperator.MULTIPLY -> {
                    dispatchProductOperation(left.value, right.value)
                }

                PrintScriptOperator.DIVIDE -> {
                    dispatchDivisionOperation(left.value, right.value)
                }
            }
        }
        return Failure("unreachable: left or right was neither Success nor Failure")
    }


    /*
    * Métodos auxiliares
    * */

    private fun dispatchSumOperation(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Either<String, PrintScriptValue> {
        return when (left
        ) {
            is NumberLiteral if right is NumberLiteral -> Success(
                handleNumberSum(
                    left.value,
                    right.value
                )
            )

            is StringLiteral if right is StringLiteral -> Success(
                handleStringSum(
                    left.value,
                    right.value
                )
            )

            else -> {
                Failure("Something went wrong")
            }
        }
    }

    private fun dispatchSubtractOperation(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Either<String, PrintScriptValue> {
        return when {
            left is NumberLiteral  //quedó horripilante
                    && right is NumberLiteral -> Success(
                handleNumberSubtract(
                    left.value,
                    right.value
                )
            )

            else -> {
                Failure("cannot subtract Strings")
            }
        }
    }

    private fun dispatchProductOperation(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Either<String, PrintScriptValue> {
        return when {
            left is NumberLiteral  //quedó horripilante
                    && right is NumberLiteral -> Success(
                handleNumberProduct(
                    left.value,
                    right.value
                )
            )

            else -> {
                Failure("cannot multiply Strings")
            }
        }
    }

    private fun dispatchDivisionOperation(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Either<String, PrintScriptValue> {
        return when {
            left is NumberLiteral  //quedó horripilante
                    && right is NumberLiteral -> Success(
                handleNumberDivide(
                    left.value,
                    right.value
                )
            )

            else -> {
                Failure("cannot divide Strings")
            }
        }
    }

    private fun stringAndNumberInOperation(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Boolean {
        return !(left is NumberLiteral && right is NumberLiteral ||
                left is StringLiteral && right is StringLiteral)
    }

    /*
    * Manejo de operaciones con tipos distintos
    * */

    private fun handleStringAndNumberSum(
        left: PrintScriptValue,
        right: PrintScriptValue
    ): Either<String, PrintScriptValue> {
        val leftText = when (left) {
            is NumberLiteral -> left.value.toString()
            is StringLiteral -> left.value
        }
        val rightText = when (right) {
            is NumberLiteral -> right.value.toString()
            is StringLiteral -> right.value
        }
        return Success(StringLiteral(leftText + rightText))
    }
}


