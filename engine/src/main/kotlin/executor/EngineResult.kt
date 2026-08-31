package executor

data class EngineResult(
    val exitCode: ExitCode,
    val context: ExecutionContext,
)
