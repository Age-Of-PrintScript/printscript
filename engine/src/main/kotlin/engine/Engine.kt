package engine

import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.Interpreter
import interpreter.LanguageSemantics
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion

class Engine {
    fun execute(
        source: String,
        io: EngineIO,
        logger: Logger,
        context: ExecutionContext = ExecutionContext(),
        version: String? = null,
    ): EngineResult {
        val psVersion =
            if (version != null) {
                PSVersion.getVersion(version).getOrReturn {
                    logFailure(it.message, logger)
                    return EngineResult(ExitCode.FAILURE, context)
                }
            } else {
                PSVersion.getLatestVersion()
            }

        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)
        val interpreter =
            Interpreter
                .new(
                    LanguageSemantics(
                        psVersion.builtInFunctions,
                        psVersion.binaryOperations,
                        psVersion.statementEvaluators,
                    ),
                )

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

        val interpreterIO = toInterpreterIO(io)

        val executionResult =
            interpreter.execute(
                (programResult as Success).value,
                interpreterIO,
                context.environment,
            )

        return when (executionResult) {
            is Failure -> {
                logFailure(executionResult.value, logger)
                EngineResult(ExitCode.FAILURE, context)
            }
            is Success -> {
                logSuccess(logger)
                EngineResult(
                    ExitCode.SUCCESS,
                    ExecutionContext(executionResult.value),
                )
            }
        }
    }

    private fun logSuccess(logger: Logger) = logger.log("Build Successful")

    private fun logFailure(
        error: Error,
        logger: Logger,
    ) = logFailure(error.toString(), logger)

    // TODO: eliminar esto
    private fun logFailure(
        error: String,
        logger: Logger,
    ) {
        logger.log(error)
        logger.log("Build Failed")
    }

    fun validate(
        source: String,
        logger: Logger,
        version: String? = null,
    ): ExitCode {
        val psVersion =
            if (version != null) {
                PSVersion.getVersion(version).getOrReturn {
                    logFailure(it.message, logger)
                    return ExitCode.FAILURE
                }
            } else {
                PSVersion.getLatestVersion()
            }
        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)

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
