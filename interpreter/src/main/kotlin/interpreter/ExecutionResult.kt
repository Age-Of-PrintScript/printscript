package interpreter

import domain.Either
import domain.Failure
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import java.util.Optional

data class ExecutionResult(
    val runtimeEnvironment: RuntimeEnvironment,
    val runtimeEvents: RuntimeEvents,
)

data class VariableInfo(
    val type: PrintScriptType,
    val value: Optional<PrintScriptValue>,
)

data class RuntimeEnvironment(
    val variableMap: Map<String, VariableInfo>,
) {
    fun getVariableMapWithValues(): Map<String, Optional<PrintScriptValue>> = variableMap.mapValues { it.value.value }

    fun addVariable(
        id: String,
        type: PrintScriptType,
        value: Optional<PrintScriptValue>,
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
        value: PrintScriptValue,
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
        value: PrintScriptValue,
    ): VariableInfo = prevValue.copy(value = Optional.of(value))

    private fun variableExists(id: String): Boolean = variableMap.containsKey(id)

    private fun hasDifferentType(
        prevValue: VariableInfo,
        value: PrintScriptValue,
    ): Boolean = prevValue.type != value.getType()
}

data class RuntimeEvents(
    val events: List<Event>,
) {
    fun addEvent(event: Event): RuntimeEvents =
        RuntimeEvents(
            events
                .toMutableList()
                .apply {
                    add(event)
                }.toList(),
        )
}

sealed interface Event

class PrintEvent(
    val message: String,
) : Event
