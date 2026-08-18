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
                            val result = env.changeVariable(ast.id.name, newValue.value)
                        }
                    }

                }
                is AST.Call -> {
                    when(val function = ast.functionName){
                        PrintScriptFunctions.PRINTLN -> {
                            events = addPrintEvent(events, function.name) // edito los events
                        }
                    }
                }
                is AST.Declaration ->{
                    if(ast.value != null){
                        when(val value = solveExpression(ast.value!!, env)){
                            is Failure -> return Failure(value.value)
                            is Success -> {
                                val newEnv = updateEnvironmentWithNewDeclaration(env, ast)  // edito el environment
                                when(newEnv){
                                    is Failure -> return Failure(newEnv.value)
                                    is Success -> {
                                        env = newEnv.value
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
       return Success(ExecutionResult(env, events))
    }
    private fun solveExpression(expression: Expression, env: RuntimeEnvironment): Either<RuntimeError, PrintScriptValue> {
        return when (val res = expressionSolver.solve(expression, env.variableMap)) {
            is Success -> Success(res.value)
            is Failure-> Failure(RuntimeError.MATH_ERROR)
        }
    }
    private fun addPrintEvent(
        events: RuntimeEvents,
        message: String
    ): RuntimeEvents {
        return events.add_event(PrintEvent(message));
    }

    private fun updateEnvironmentWithNewDeclaration(env: RuntimeEnvironment, ast: AST.Declaration): Either<RuntimeError, RuntimeEnvironment> {

        val variables = env.variableMap.toMutableMap()
        val id = ast.id
        val expression = ast.value

        if(variables.containsKey(id.toString())){ // Estoy declarando una variable que ya está definida, error. Esto ya se hace en add_variable, habría que usar ese metodo.
            return Failure(RuntimeError.VARIABLE_ALREADY_DEFINED)
        }

        if (expression == null) {
            variables[id.toString()] = Optional.empty()
            return Success(
                    (RuntimeEnvironment(
                    variables)))
        }

        when(val result = solveExpression(expression, env)){
            is Failure -> return Failure(result.value)
            is Success -> {
                val type = expressionSolver.getExpressionScriptType(result.value)
                if (!type.equals(ast.type.name)){
                    return Failure(RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE)
                }
                variables[id.toString()] = Optional.of(result.value)
                return Success(RuntimeEnvironment(variables))
            }
        }
    }
}
