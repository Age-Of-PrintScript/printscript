package executor

import ast.Program
import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.ExecutionResult
import interpreter.Interpreter
import interpreter.InterpreterImpl
import interpreter.PrintEvent
import interpreter.RuntimeEnvironment
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl

class Engine {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val interpreter: Interpreter = InterpreterImpl()
    private var environment: RuntimeEnvironment? = null

    fun execute(
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
        val executionResult = interpreter.execute((programResult as Success).value)
        return when (executionResult) {
            is Failure -> {
                logFailure(executionResult.value, logger)
                ExitCode.FAILURE
            }
            is Success -> {
                logSuccess(executionResult.value, logger)
                ExitCode.SUCCESS
            }
        }
    }

    private fun logSuccess(
        result: ExecutionResult,
        logger: Logger,
    ) {
        val events = result.runtimeEvents
        environment = result.runtimeEnvironment
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

    fun validate(source: String): Either<Error, Program> {
        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }
        return Success(program)
    }
}
