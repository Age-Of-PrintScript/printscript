package engine

import domain.Error
import domain.Failure
import domain.Success
import engine.ps_versions.v1_0.v1_0Keywords
import engine.ps_versions.v1_0.v1_0Symbols
import engine.ps_versions.v1_0.v1_0ValidAST
import interpreter.Interpreter
import interpreter.environment.ExecutionResult
import interpreter.environment.PrintEvent
import lexer.Lexer
import lexer.Lexicon
import parser.Parser

class Engine {
    private val lexer = Lexer.new(Lexicon(v1_0Symbols, v1_0Keywords))
    private val parser = Parser.new(v1_0ValidAST)
    private val interpreter = Interpreter.new(v1_0semantics)

    fun execute(
        source: String,
        logger: Logger,
        context: ExecutionContext = ExecutionContext(),
    ): EngineResult {
        val tokensResult = lexer.tokenize(source)
        if (tokensResult is Failure) {
            logFailure(tokensResult.value, logger)
            return EngineResult(ExitCode.FAILURE, context)
        }
        val programResult = parser.parse((tokensResult as Success).value)
        if (programResult is Failure) {
            logFailure(programResult.value, logger)
            return EngineResult(ExitCode.FAILURE, context)
        }
        val executionResult =
            interpreter.executeWithEnvironment(
                (programResult as Success).value,
                context.environment,
            )
        return when (executionResult) {
            is Failure -> {
                logFailure(executionResult.value, logger)
                EngineResult(ExitCode.FAILURE, context)
            }
            is Success -> {
                logSuccess(executionResult.value, logger)
                EngineResult(
                    ExitCode.SUCCESS,
                    ExecutionContext(executionResult.value.runtimeEnvironment),
                )
            }
        }
    }

    private fun logSuccess(
        result: ExecutionResult,
        logger: Logger,
    ) {
        val events = result.runtimeEvents
        for (event in events.events) {
            when (event) {
                is PrintEvent -> logger.log(event.message)
            }
        }
        logger.log("Build Successful")
    }

    private fun logFailure(
        error: Error,
        logger: Logger,
    ) {
        logger.log(error.toString())
        logger.log("Build Failed")
    }

    fun validate(
        source: String,
        logger: Logger,
    ): ExitCode {
        val tokensResult = lexer.tokenize(source)
        if (tokensResult is Failure) {
            logFailure(tokensResult.value, logger)
            return ExitCode.FAILURE
        }
        val programResult = parser.parse((tokensResult as Success).value)
        if (programResult is Failure) {
            logFailure(programResult.value, logger)
            return ExitCode.FAILURE
        }
        logger.log("Validation Successful")
        return ExitCode.SUCCESS
    }
}
