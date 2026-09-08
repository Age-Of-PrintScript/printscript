package interpreter.environment

import ast.Expression.Literal
import domain.Either
import domain.Failure
import domain.PSType
import domain.Success
import interpreter.RuntimeError

data class OldRuntimeEnvironment(
    val variableMap: Map<String, OldVariableInfo>,
)

data class RuntimeEnvironment(
    val variableMap: Map<String, VariableInfo>,
) {
    fun getVariableMapWithValues(): Map<String, Literal?> = variableMap.mapValues { it.value.value }

    fun addVariable(
        id: String,
        type: PSType,
        value: Literal?,
    ): Either<RuntimeError, RuntimeEnvironment> {
        if (variableMap.containsKey(id)) return Failure(RuntimeError.VARIABLE_ALREADY_DEFINED)
        return Success(
            RuntimeEnvironment(
                variableMap
                    .toMutableMap()
                    .apply {
                        put(id, VariableInfo(type, value))
                    }.toMap(),
            ),
        )
    }

    fun changeVariable(
        id: String,
        value: Literal,
    ): Either<RuntimeError, RuntimeEnvironment> {
        if (!variableExists(id)) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)

        val prevValue = variableMap.getValue(id)

        if (hasDifferentType(prevValue, value)) return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)

        return Success(
            RuntimeEnvironment(
                variableMap
                    .toMutableMap()
                    .apply {
                        put(id, updateValue(prevValue, value))
                    }.toMap(),
            ),
        )
    }

    private fun updateValue(
        prevValue: VariableInfo,
        value: Literal,
    ): VariableInfo = prevValue.copy(value = value)

    private fun variableExists(id: String): Boolean = variableMap.containsKey(id)

    private fun hasDifferentType(
        prevValue: VariableInfo,
        value: Literal,
    ): Boolean = prevValue.type != value.type
}
