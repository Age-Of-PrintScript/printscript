package executor

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import interpreter.ExecutionResult
import interpreter.Interpreter
import interpreter.InterpreterImpl
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl

class Executor {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val interpreter: Interpreter = InterpreterImpl()

    fun execute(source: String): Either<Error, ExecutionResult> {
        val tokens = lexer.tokenize(source)
        if (tokens is Failure) return Failure(tokens.value)
        val program = parser.parse((tokens as Success).value)
        if (program is Failure) return Failure(program.value)
        val executionResult = interpreter.execute((program as Success).value)
        return when (executionResult) {
            is Failure -> Failure(executionResult.value)
            is Success -> Success(executionResult.value)
        }
    }
}
