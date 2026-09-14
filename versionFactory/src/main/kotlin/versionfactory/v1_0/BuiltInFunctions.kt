package versionfactory.v1_0

import domain.Success
import interpreter.BuiltInFunction

internal val printlnFunction =
    BuiltInFunction { args, io ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        io.emitter.print(message)
        Success(null)
    }

internal val v1_0builtInFunctions: Map<String, BuiltInFunction> =
    mapOf(
        "println" to printlnFunction,
    )
