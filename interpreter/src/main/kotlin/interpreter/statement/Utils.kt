package interpreter.statement

import ast.Expression
import ast.Expression.Literal
import domain.Either
import domain.Failure
import domain.PSLiteral
import domain.PSType
import domain.Success
import domain.getOrReturn
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.Event
import interpreter.environment.RuntimeEnvironment

data class ExpressionResult(
    val returnValue: PSLiteral? = null,
    val events: List<Event> = emptyList(),
)

// es necesario que estoo se instancie con cada nuevo interpreterimpl?
private val expressionSolver = ExpressionSolver()

internal fun solveExpression(
    expression: Expression,
    env: RuntimeEnvironment,
    semantics: LanguageSemantics,
): Either<RuntimeError, ExpressionResult> {
    val result =
        expressionSolver
            .solve(expression, env.getVariableMapWithValues(), semantics)
            .getOrReturn { return Failure(RuntimeError.MATH_ERROR) }

    return Success(result)
}

internal fun updateEnvironmentWithNewDeclaration(
    env: RuntimeEnvironment,
    id: String,
    type: PSType,
    value: Literal,
    mutable: Boolean,
): Either<RuntimeError, RuntimeEnvironment> {
    if (type != value.type) {
        return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
    }
    val newEnv =
        env
            .addVariable(id, type, value, mutable)
            .getOrReturn { return Failure(it) }

    return Success(newEnv)
}

internal fun Literal.toPSLiteral(): PSLiteral = PSLiteral(raw = value, type = type)

internal fun PSLiteral.toLiteral(): Literal = Literal(value = raw, type = type)
