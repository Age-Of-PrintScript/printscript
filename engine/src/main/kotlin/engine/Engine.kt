package engine

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import interpreter.Interpreter
import interpreter.LanguageSemantics
import interpreter.environment.ExecutionResult
import interpreter.environment.PrintEvent
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionFactory.PSVersion
import versionFactory.PSVersion1_0

class Engine {
    fun execute(
        source: String,
        logger: Logger,
        context: ExecutionContext = ExecutionContext(),
        version: String? = null,
    ): EngineResult {
        val version = PSVersion1_0()

        val lexer = Lexer.new(Lexicon(version.symbols, version.keywords))
        val parser = Parser.new(version.statementParsers)
        val interpreter = Interpreter.new(LanguageSemantics(version.builtInFunctions, version.binaryOperations, version.statementEvaluators))

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
        val version = PSVersion1_0()
        val lexer = Lexer.new(Lexicon(version.symbols, version.keywords))
        val parser = Parser.new(version.statementParsers)

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

    private fun getVersion(version: String): Either<String, PSVersion> {
        return when (version) {
            "1.0" -> Success(PSVersion1_0())
            else -> Failure("Unknown version")
        }
    }
    private fun getLatestVersion() = PSVersion1_0()
}
