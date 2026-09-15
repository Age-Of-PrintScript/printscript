package interpreter

import domain.Error
import domain.Position

data class RuntimeError(
    val reason: String,
    override val start: Position? = null,
    override val end: Position? = null,
) : Error {
    override fun getMessage(): String = reason

    fun withPosition(
        start: Position?,
        end: Position? = null,
    ): RuntimeError = copy(start = start, end = end)

    companion object {
        val VARIABLE_ALREADY_DEFINED = RuntimeError("Variable already defined")
        val VARIABLE_DOESNT_EXIST = RuntimeError("Variable doesn't exist")
        val VARIABLE_NOT_INITIALIZED = RuntimeError("Variable is not initialized")
        val UNSUPPORTED_OPERATION = RuntimeError("Unsupported operation")
        val FUNCTION_NOT_FOUND = RuntimeError("Function not found")
        val MATH_ERROR = RuntimeError("cannot resolve operation")
        val VARIABLE_HAS_DIFFERENT_TYPE = RuntimeError("That variable exists with a different type")
        val STRING_REPETITION_REQUIRES_INT = RuntimeError("string repetition requires a positive integer")
        val MISSING_ASSIGNATION = RuntimeError("Variable cannot be assigned to void")
        val MISSING_EVALUATOR_FOR_AST = RuntimeError("AST evaluator not found")
        val INVALID_CAST = RuntimeError("Cannot cast value to the expected type")
        val VARIABLE_NOT_MUTABLE = RuntimeError("Variable is not mutable")
        val MISSING_IF_CONDITION = RuntimeError("Condition not found")
        val MISSING_ARGUMENT = RuntimeError("Missing required argument")
        val ENV_VARIABLE_NOT_FOUND = RuntimeError("Environment variable not found")
    }
}
