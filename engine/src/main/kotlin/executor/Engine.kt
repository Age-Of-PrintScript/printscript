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
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl

class Engine {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val interpreter: Interpreter = InterpreterImpl()

    fun execute(source: String): Either<Error, ExecutionResult> {
        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }
        val executionResult = interpreter.execute(program)
        return when (executionResult) {
            is Failure -> Failure(executionResult.value)
            is Success -> Success(executionResult.value)
        }
    }

    fun validate(source: String): Either<Error, Program> {
        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }
        return Success(program)
    }
}
