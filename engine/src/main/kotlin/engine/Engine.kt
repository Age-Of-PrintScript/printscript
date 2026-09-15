package engine

import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.Interpreter
import interpreter.LanguageSemantics
import lexer.Lexer
import lexer.LexerError
import lexer.Lexicon
import parser.Parser
import parser.tokenConsumers.TokenConsumer
import versionfactory.PSVersion
import java.io.Reader
import java.io.StringReader

class Engine {
    fun execute(
        source: String,
        io: EngineIO,
        logger: Logger,
        context: ExecutionContext = ExecutionContext(),
        version: String? = null,
    ): EngineResult = execute(StringReader(source), io, logger, context, version)

    fun execute(
        reader: Reader,
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
                        psVersion.typeCasters,
                    ),
                )

        val interpreterIO = toInterpreterIO(io)
        var currentEnv = context.environment
        var lexerError: LexerError? = null

        val consumer =
            TokenConsumer.from {
                if (lexerError != null) return@from null
                when (val result = lexer.nextToken(reader)) {
                    null -> null
                    is Success -> result.value
                    is Failure -> {
                        lexerError = result.value
                        null
                    }
                }
            }

        while (true) {
            val parseResult = parser.parseNext(consumer)
            if (lexerError != null) {
                logFailure(lexerError!!, logger)
                return EngineResult(ExitCode.FAILURE, ExecutionContext(currentEnv))
            }
            if (parseResult == null) {
                logSuccess(logger)
                return EngineResult(ExitCode.SUCCESS, ExecutionContext(currentEnv))
            }
            val statement =
                parseResult.getOrReturn { syntaxError ->
                    logFailure(syntaxError, logger)
                    return EngineResult(ExitCode.FAILURE, ExecutionContext(currentEnv))
                }

            val execResult = interpreter.executeStatement(statement, interpreterIO, currentEnv)
            currentEnv =
                execResult.getOrReturn { runtimeError ->
                    logFailure(runtimeError, logger)
                    return EngineResult(ExitCode.FAILURE, ExecutionContext(currentEnv))
                }
        }
    }

    private fun logSuccess(logger: Logger) = logger.log("Build Successful")

    private fun logFailure(
        error: Error,
        logger: Logger,
    ) = logFailure(formatError(error), logger)

    private fun formatError(error: Error): String {
        val start = error.start
        return if (start != null) {
            "[$start] ${error.getMessage()}"
        } else {
            error.getMessage()
        }
    }

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
    ): ExitCode = validate(StringReader(source), logger, version)

    fun validate(
        reader: Reader,
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

        val tokensResult = lexer.tokenize(reader.readText())
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
