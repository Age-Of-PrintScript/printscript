package engine

data class EngineResult(
    val exitCode: ExitCode,
    val context: ExecutionContext,
)
