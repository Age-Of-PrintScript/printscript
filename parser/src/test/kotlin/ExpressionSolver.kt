
internal class ExpressionSolver { //clase auxiliar para hacer tests mas faciles. Solo sirve para expresiones sin variables

    internal fun solve(expression: Expression): Int = when (expression) {
            is Num ->  expression.number
            is Variable -> throw IllegalArgumentException("Unexpected expression")
            is Operation -> solveOperation(expression)
            }


    private fun solveOperation(operation: Operation): Int {
        val left = solve(operation.left)
        val right = solve(operation.right)

        return when (operation.operator) {
            PrintScriptOperator.SUM -> left + right
            PrintScriptOperator.SUBTRACT -> left - right
            PrintScriptOperator.MULTIPLY -> left * right
            PrintScriptOperator.DIVIDE -> left / right
        }

    }

}