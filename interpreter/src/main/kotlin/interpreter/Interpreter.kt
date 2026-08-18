package interpreter

import ast.AST
import ast.Expression
import ast.ExpressionSolver
import domain.Either
import ast.Program
import domain.Failure
import domain.PrintScriptFunctions
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import java.util.Optional

interface Interpreter {
    fun execute(program: Program): Either<RuntimeError, ExecutionResult>
    fun executeWithEnvironment(program: Program, runtimeEnvironment: RuntimeEnvironment): Either<RuntimeError, ExecutionResult>
}

class InterpreterImpl: Interpreter {
    val expressionSolver = ExpressionSolver()
    override fun execute(program: Program): Either<RuntimeError, ExecutionResult> {
        return execute(program, RuntimeEnvironment(emptyMap()), RuntimeEvents(emptyList()))
    }

    override fun executeWithEnvironment(
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
                    when(val newValue = solveExpression(ast.value, env)){
                        is Failure -> return Failure(newValue.value)
                        is Success -> {
                            val result = env.change_variable(ast.id.name, newValue.value)
                        }
                    }

                }
                is AST.Call -> {
                    when(val function = ast.functionName){
                        PrintScriptFunctions.PRINTLN -> {
                            events = addPrintEvent(events, function.name)
                        }
                    }
                }
                is AST.Declaration ->{
                    if(ast.value != null){
                        when(val value = solveExpression(ast.value!!, env)){
                            is Failure -> return Failure(value.value)
                            is Success -> TODO()
                        }
                    }

                }
            }
        }
        TODO()
    }
    private fun solveExpression(expression: Expression, env: RuntimeEnvironment): Either<RuntimeError, PrintScriptValue> {
        val res = expressionSolver.solve(expression, env.variableMap)
        TODO()
    }
    private fun addPrintEvent(
        events: RuntimeEvents,
        message: String
    ): RuntimeEvents {
        return events.add_event(PrintEvent(message));
    }

    private fun updateEnvironmentWithNewDeclaration(env: RuntimeEnvironment, events: RuntimeEvents, ast: AST.Declaration): Either<RuntimeError, ExecutionResult> {

        val variables = env.variableMap.toMutableMap()
        val id = ast.id
        val expression = ast.value

        if(variables.containsKey(id.toString())){
            return Failure(RuntimeError.VARIABLE_ALREADY_DEFINED)
        }

        if (expression == null) {
            variables[id.toString()] = TODO()

        }

        val result = solveExpression(expression, env)


        if(ast.type.equals(TODO())){
            return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
        }


        //return when (expression) {
          //  is Success -> Success(ExecutionResult(
           // RuntimeEnvironment(variables),
            events
            //))
            //is Failure -> Failure(expression)
        //}
    }


}
