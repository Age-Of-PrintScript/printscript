package interpreter.statement

import ast.Expression
import ast.Expression.Literal
import ast.ExpressionSolver
import domain.Either
import domain.Failure
import domain.PSType
import domain.Success
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment

// es necesario que estoo se instancie con cada nuevo interpreterimpl?
val expressionSolver = ExpressionSolver()

internal fun solveExpression(
    expressionViejo: Expression,
    env: RuntimeEnvironment,
): Either<RuntimeError, Literal> =
    when (val res = expressionSolver.solve(expressionViejo, env.getVariableMapWithValues())) {
        is Success -> Success(res.value)
        is Failure -> Failure(RuntimeError.MATH_ERROR)
    }

internal fun updateEnvironmentWithNewDeclaration(
    env: RuntimeEnvironment,
    id: String,
    type: PSType,
    value: Literal,
): Either<RuntimeError, RuntimeEnvironment> {
    if (type != value.type) {
        return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
    }
    return when (val newEnv = env.addVariable(id, type, value)) {
        is Failure -> Failure(newEnv.value)
        is Success -> Success(newEnv.value)
    }
}
