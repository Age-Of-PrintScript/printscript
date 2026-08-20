package interpreter.cases

import ast.Program
import domain.Error
import interpreter.RuntimeEnvironment
import interpreter.RuntimeEvents

data class SuccessCase(
    val name: String,
    val program: Program,
    val expectedEnv: RuntimeEnvironment,
    val expectedEvents: RuntimeEvents,
)

data class FailureCase(
    val name: String,
    val program: Program,
    val expectedFailure: Error,
)

class InterpreterTest {
}