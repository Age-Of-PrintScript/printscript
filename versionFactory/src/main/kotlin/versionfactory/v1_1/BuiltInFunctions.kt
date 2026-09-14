package versionfactory.v1_1

import domain.Failure
import domain.PSLiteral
import domain.StrType
import domain.Success
import interpreter.BuiltInFunction
import interpreter.RuntimeError
import versionfactory.v1_0.v1_0builtInFunctions

internal val readInputFunction =
    BuiltInFunction { args, io ->
        val prompt = if (args.isNotEmpty()) args.first().raw else ""
        val input = io.provider.readInput(prompt)
        Success(PSLiteral(input, StrType))
    }

internal val readEnvFunction =
    BuiltInFunction { args, io ->
        val key =
            args.firstOrNull()?.raw
                ?: return@BuiltInFunction Failure(RuntimeError.MISSING_ARGUMENT)
        val value =
            io.envProvider.readEnv(key)
                ?: return@BuiltInFunction Failure(RuntimeError.ENV_VARIABLE_NOT_FOUND)
        Success(PSLiteral(value, StrType))
    }

internal val v1_1builtInFunctions: Map<String, BuiltInFunction> =
    v1_0builtInFunctions +
        mapOf(
            "readInput" to readInputFunction,
            "readEnv" to readEnvFunction,
        )
