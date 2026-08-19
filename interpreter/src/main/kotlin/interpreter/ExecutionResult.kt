package interpreter

import domain.Either
import domain.Failure
import domain.PrintScriptValue
import domain.Success
import java.util.Optional


data class ExecutionResult(
    val runtimeEnvironment: RuntimeEnvironment,
    val runtimeEvents: RuntimeEvents
)


data class RuntimeEnvironment(
    val variableMap: Map<String, Optional<PrintScriptValue>>
){
    fun addVariable(id: String, value: Optional<PrintScriptValue>): Either<RuntimeError, RuntimeEnvironment> {

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
    fun changeVariable(id: String, value: PrintScriptValue): Either<RuntimeError, RuntimeEnvironment> {
        if(!variableMap.containsKey(id)) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)

        if(variableMap.get(id)!!.get().getType() != value.getType()) return Failure(RuntimeError.VARIABLE_DOESNT_EXIST)
        return Success(RuntimeEnvironment(
            variableMap
            .toMutableMap()
            .apply {
                put(id, Optional.of(value))
            }
            .toMap()
            )
        )
    }
}

data class RuntimeEvents(val events: List<Event>){

    fun add_event(event: Event): RuntimeEvents {
        return RuntimeEvents(events
            .toMutableList()
            .apply {
                add(event)
            }
            .toList()
        )
    }
}


sealed interface Event
class PrintEvent(val message: String) : Event;

