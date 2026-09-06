package linter.rules

import ast.ASTViejo
import ast.ASTViejo.Call
import ast.ExpressionViejo
import domain.Position
import domain.PrintScriptFunctions
import kotlinx.serialization.json.JsonObject
import linter.LinterRule
import linter.LinterRuleFactory
import linter.Warning

internal class PrintlnArgumentRule : LinterRule {
    override fun apply(astViejo: ASTViejo): Warning? {
        if (notAPrintCall(astViejo)) return null
        val astCall = astViejo as Call
        val arg = astCall.args.firstOrNull() ?: return null
        return if (argIsNotExpression(arg)) {
            null
        } else {
            Warning(
                "println must be called with an identifier or literal",
                Position(0, 0),
            )
        }
    }

    private fun argIsNotExpression(arg: ExpressionViejo) = arg is ExpressionViejo.Variable || arg is ExpressionViejo.Literal

    private fun notAPrintCall(astViejo: ASTViejo) = astViejo !is Call || astViejo.functionName != PrintScriptFunctions.PRINTLN
}

internal object PrintlnArgumentRuleFactory : LinterRuleFactory {
    override val ruleName = "println-no-expression"

    override fun fromConfig(params: JsonObject): LinterRule = PrintlnArgumentRule()
}
