package interpreter

import domain.Either
import domain.Failure
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success


data class ExecutionResult(
    val runtimeEnvironment: RuntimeEnvironment,
    val runtimeEvents: RuntimeEvents
)


data class RuntimeEnvironment(
    private val variableMap: Map<String, PrintScriptValue>
){
    fun add_variable(id: String, value: PrintScriptValue): Either<RuntimeError, RuntimeEnvironment> {

        if(variableMap.containsKey(id)) return Failure(RuntimeError.VARIABLE_ALREADY_DEFINED)

        return Success(
            RuntimeEnvironment(
                variableMap
                    .toMutableMap()
                    .apply {
                        put(id, value)
                    }
                    .toMap()
            )
        )
    }
    fun change_variable(id: String, value: PrintScriptValue): Either<RuntimeError, RuntimeEnvironment> {
        if(!variableMap.containsKey(id)) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)
        if(variableMap.get(id)!!.getType() != value.getType()) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)
        return Success(RuntimeEnvironment(
            variableMap
            .toMutableMap()
            .apply {
                put(id, value)
            }
            .toMap()
            )
        )
    }
}

data class RuntimeEvents(val events: List<Events>){
    fun add_event(event: Events): RuntimeEvents {
        return RuntimeEvents(events
            .toMutableList()
            .apply {
                add(event)
            }
            .toList()
        )
    }
}


sealed interface Events
class PrintEvent(val message: String) : Events;

