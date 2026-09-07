package engine.ps_versions.v1_0

import domain.Success
import interpreter.BuiltInFunction
import interpreter.FunctionResult
import interpreter.PrintEvent

val printlnFunction =
    BuiltInFunction { args ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        Success(
            FunctionResult(
                returnValue = null,
                events = listOf(PrintEvent(message)),
            ),
        )
    }

val v1_0builtInFunctions: Map<String, BuiltInFunction> =
    mapOf(
        "println" to printlnFunction,
    )
