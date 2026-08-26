package interpreter

import ast.AST
import ast.Expression
import ast.ExpressionSolver
import ast.Program
import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import java.util.Optional

interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>

    fun executeWithEnvironment(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
    ): Either<RuntimeError, ExecutionResult>
}

class InterpreterImpl : Interpreter {
    val expressionSolver = ExpressionSolver()

    override fun execute(program: Program): Either<RuntimeError, ExecutionResult> = execute(program, RuntimeEnvironment(emptyMap()), RuntimeEvents(emptyList()))

    override fun executeWithEnvironment(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
    ): Either<RuntimeError, ExecutionResult> = execute(program, runtimeEnvironment, RuntimeEvents(emptyList()))

    private fun execute(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
        runtimeEvents: RuntimeEvents,
    ): Either<RuntimeError, ExecutionResult> {
        val asts = program.trees
        var events = runtimeEvents
        var env = runtimeEnvironment
        for (ast in asts) {
            when (ast) {
                is AST.Assignment -> {
                    when (val newValue = solveExpression(ast.value, env)) {
                        is Failure -> return Failure(newValue.value)
                        is Success -> {
                            when (val changedEnvResult = env.changeVariable(ast.id.name, newValue.value)) {
                                is Failure -> return Failure(changedEnvResult.value)
                                is Success -> {
                                    env = changedEnvResult.value
                                }
                            }
                        }
                    }
                }
                is AST.Call -> {
                    when (ast.functionName) {
                        PrintScriptFunctions.PRINTLN -> {
                            val solvedResult = solveExpression(ast.args.first(), env)
                            when (solvedResult) {
                                is Failure -> return Failure(solvedResult.value)
                                is Success -> {
                                    events = addPrintEvent(events, valueToString(solvedResult.value))
                                }
                            }
                            // edito los events
                        }
                    }
                }
                is AST.Declaration -> {
                    if (ast.value != null) {
                        when (val solvedResult = solveExpression(ast.value!!, env)) {
                            is Failure -> return Failure(solvedResult.value)
                            is Success -> {
                                val newEnv =
                                    updateEnvironmentWithNewDeclaration(env, ast.id.name, ast.type.name, solvedResult.value) // edito el environment
                                when (newEnv) {
                                    is Failure -> return Failure(newEnv.value)
                                    is Success -> {
                                        env = newEnv.value
                                    }
                                }
                            }
                        }
                    } else {
                        val newEnv = env.addVariable(ast.id.name, ast.type.name, Optional.empty())
                        when (newEnv) {
                            is Failure -> return Failure(newEnv.value)
                            is Success -> {
                                env = newEnv.value
                            }
                        }
                    }
                }
            }
        }
        return Success(ExecutionResult(env, events))
    }

    private fun solveExpression(
        expression: Expression,
        env: RuntimeEnvironment,
    ): Either<RuntimeError, PrintScriptValue> =
        when (val res = expressionSolver.solve(expression, env.getVariableMapWithValues())) {
            is Success -> Success(res.value)
            is Failure -> Failure(RuntimeError.MATH_ERROR)
        }

    private fun addPrintEvent(
        events: RuntimeEvents,
        message: String,
    ): RuntimeEvents = events.addEvent(PrintEvent(message))

    private fun updateEnvironmentWithNewDeclaration(
        env: RuntimeEnvironment,
        id: String,
        type: PrintScriptType,
        value: PrintScriptValue,
    ): Either<RuntimeError, RuntimeEnvironment> {
        if (type != value.getType()) {
            return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
        }
        return when (val newEnv = env.addVariable(id, type, Optional.of(value))) {
            is Failure -> Failure(newEnv.value)
            is Success -> Success(newEnv.value)
        }
    }

    private fun valueToString(value: PrintScriptValue): String =
        when (value) {
            is PrintScriptValue.NumberLiteral -> value.value.toString()
            is PrintScriptValue.StringLiteral -> value.value
        }
}
