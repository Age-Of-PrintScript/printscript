package executor

import domain.Error
import domain.Failure
import domain.Success
import interpreter.ExecutionResult
import interpreter.Interpreter
import interpreter.InterpreterImpl
import interpreter.PrintEvent
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl

class Engine {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val interpreter: Interpreter = InterpreterImpl()

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
