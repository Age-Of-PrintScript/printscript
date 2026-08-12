package interpreter

import ast.AST
import domain.Either
import ast.Program
import domain.PrintScriptFunctions

interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>
    fun execute_with_environment(program: Program, runtimeEnvironment: RuntimeEnvironment): Either<RuntimeError, ExecutionResult>
}

class InterpreterImpl: Interpreter {
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
        var runtimeEvents = runtimeEvents
        var runtimeEnvironment = runtimeEnvironment
        for(ast in asts) {
            when(ast){
                is AST.Assignment -> {

                }
                is AST.Call -> {
                    when(val function = ast.functionName){
                        PrintScriptFunctions.PRINTLN -> {
                            runtimeEvents = add_print_event(runtimeEvents, function.name)
                        }
                    }
                }
                is AST.Declaration ->{

                }
            }
        }
    }
}

private fun add_print_event(
    events: RuntimeEvents,
    message: String
): RuntimeEvents {
    return events.add_event(PrintEvent(message));
}
