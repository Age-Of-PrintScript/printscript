package interpreter

import ast.AST
import ast.Expression
import ast.ExpressionSolver
import domain.Either
import ast.Program
import domain.Failure
import domain.PrintScriptFunctions
import domain.PrintScriptValue
import domain.Success

interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>
    fun execute_with_environment(program: Program, runtimeEnvironment: RuntimeEnvironment): Either<RuntimeError, ExecutionResult>
}

class InterpreterImpl: Interpreter {
    val expressionSolver = ExpressionSolver()
    override fun execute(program: Program): Either<RuntimeError, ExecutionResult> {
        return execute(program, RuntimeEnvironment(emptyMap()), RuntimeEvents(emptyList()))
    }

    override fun execute_with_environment(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment
    ): Either<RuntimeError, ExecutionResult> {
        return execute(program, runtimeEnvironment, RuntimeEvents(emptyList()))
    }

    private fun execute(
        program: Program,
        runtimeEnvironment: RuntimeEnvironment,
        runtimeEvents: RuntimeEvents
    ): Either<RuntimeError, ExecutionResult> {
        val asts = program.trees
        var events = runtimeEvents
        var env = runtimeEnvironment
        for(ast in asts) {
            when(ast){
                is AST.Assignment -> {
                    when(val newValue = solve_expression(ast.value, env)){
                        is Failure -> return Failure(newValue.value)
                        is Success -> {
                            val result = env.change_variable(ast.id.name, newValue.value)
                        }
                    }

                }
                is AST.Call -> {
                    when(val function = ast.functionName){
                        PrintScriptFunctions.PRINTLN -> {
                            events = add_print_event(events, function.name)
                        }
                    }
                }
                is AST.Declaration ->{
                    if(ast.value != null){
                        when(val value = solve_expression(ast.value!!, env)){
                            is Failure -> return Failure(value.value)
                            is Success -> TODO()
                        }
                    }

                }
            }
        }
        TODO()
    }
    private fun solve_expression(expression: Expression, env: RuntimeEnvironment): Either<RuntimeError, PrintScriptValue> {
        val res = expressionSolver.solve(expression, env.variableMap)
        TODO()
    }
    private fun add_print_event(
        events: RuntimeEvents,
        message: String
    ): RuntimeEvents {
        return events.add_event(PrintEvent(message));
    }
}
