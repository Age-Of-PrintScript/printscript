package interpreter.environment

import domain.Either
import domain.Failure
import domain.PSLiteral
import domain.PSType
import domain.Success
import interpreter.RuntimeError

data class RuntimeEnvironment(
    private val variableMap: Map<String, VariableInfo> = emptyMap(),
) {
    fun getVariableMapWithValues(): Map<String, PSLiteral?> = variableMap.mapValues { it.value.value }

    fun getVariableType(id: String): PSType? = variableMap[id]?.type

    fun getVariableValue(id: String): PSLiteral? = variableMap[id]?.value

    fun getVariable(id: String): VariableInfo? = variableMap[id]

    fun containsVariable(id: String): Boolean = variableMap.containsKey(id)

    fun addVariable(
        id: String,
        type: PSType,
        value: PSLiteral?,
        mutable: Boolean,
    ): Either<RuntimeError, RuntimeEnvironment> {
        if (containsVariable(id)) return Failure(RuntimeError.VARIABLE_ALREADY_DEFINED)
        return Success(
            RuntimeEnvironment(
                variableMap + (id to VariableInfo(type, value, mutable)),
            ),
        )
    }

    fun changeVariable(
        id: String,
        value: PSLiteral,
    ): Either<RuntimeError, RuntimeEnvironment> {
        if (!containsVariable(id)) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)

        val prevValue = variableMap.getValue(id)

        if (!prevValue.mutable) return Failure(RuntimeError.VARIABLE_NOT_MUTABLE)

        if (hasDifferentType(prevValue, value)) return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)

        return Success(
            RuntimeEnvironment(
                variableMap + (id to VariableInfo(value.type, value, true)),
            ),
        )
    }

    private fun hasDifferentType(
        prevValue: VariableInfo,
        value: PSLiteral,
    ): Boolean = prevValue.type != value.type
}
