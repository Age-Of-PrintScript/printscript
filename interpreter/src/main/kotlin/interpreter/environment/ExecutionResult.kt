package interpreter.environment

data class ExecutionResult(
    val runtimeEnvironment: RuntimeEnvironment,
    val runtimeEvents: RuntimeEvents,
)
