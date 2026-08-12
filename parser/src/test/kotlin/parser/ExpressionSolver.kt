package parser

import ast.Expression
import domain.PrintScriptOperator
import domain.PrintScriptValue

internal class ExpressionSolver {

    internal fun solve(expression: Expression): Number = when (expression) {
            is Expression.Literal ->  {
                when (val scriptValue = expression.value){
                    is PrintScriptValue.NumberLiteral -> scriptValue.value
                    is PrintScriptValue.StringLiteral -> TODO()
                }
            }
            is Expression.Variable -> throw IllegalArgumentException("Unexpected expression")
            is Expression.Operation -> solveOperation(expression)
            }


    private fun solveOperation(operation: Expression.Operation): Number {
        val left = solve(operation.left).toDouble()
        val right = solve(operation.right).toDouble()

        return when (operation.operator) {
            PrintScriptOperator.SUM -> left + right
            PrintScriptOperator.SUBTRACT -> left - right
            PrintScriptOperator.MULTIPLY -> left * right
            PrintScriptOperator.DIVIDE -> left / right
            PrintScriptOperator.OPEN_PARENTHESIS -> TODO()
            PrintScriptOperator.CLOSE_PARENTHESIS -> TODO()
        }

    }

}