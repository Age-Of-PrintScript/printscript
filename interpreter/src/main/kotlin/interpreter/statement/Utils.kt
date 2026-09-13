package interpreter.statement

import ast.Expression
import ast.Expression.Literal
import domain.Either
import domain.Failure
import domain.PSLiteral
import domain.PSType
import domain.Success
import domain.getOrReturn
import interpreter.CastKey
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment

private val expressionSolver = ExpressionSolver()

internal fun solveExpression(
    expression: Expression,
    env: RuntimeEnvironment,
    io: InterpreterIO,
    semantics: LanguageSemantics,
): Either<RuntimeError, PSLiteral?> {
    val result =
        expressionSolver
            .solve(expression, env.getVariableMapWithValues(), io, semantics)
            .getOrReturn { return Failure(RuntimeError.MATH_ERROR) }

    return Success(result)
}

internal fun resolveWithCast(
    value: PSLiteral,
    targetType: PSType,
    expression: Expression,
    semantics: LanguageSemantics,
): Either<RuntimeError, PSLiteral> =
    if (value.type != targetType) {
        if (expression is Expression.Call) {
            val caster =
                semantics.typeCasters[CastKey(value.type, targetType)]
                    ?: return Failure(RuntimeError.INVALID_CAST)
            caster.cast(value)
        } else {
            Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
        }
    } else {
        Success(value)
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
