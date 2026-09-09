package versionfactory.v1_0

import domain.Success
import interpreter.BuiltInFunction
import interpreter.FunctionResult
import interpreter.environment.PrintEvent

internal val printlnFunction =
    BuiltInFunction { args ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        Success(
            FunctionResult(
                returnValue = null,
                events = listOf(PrintEvent(message)),
            ),
        )
    }

internal val v1_0builtInFunctions: Map<String, BuiltInFunction> =
    mapOf(
        "println" to printlnFunction,
    )
