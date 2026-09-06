package executor.ps_versions.v1_0

import domain.Success
import interpreter.Function
import interpreter.FunctionResult
import interpreter.PrintEvent

val printlnFunction =
    Function { args ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        Success(
            FunctionResult(
                returnValue = null,
                events = listOf(PrintEvent(message)),
            ),
        )
    }

val v1_0builtInFunctions: Map<String, Function> =
    mapOf(
        "println" to printlnFunction,
    )
