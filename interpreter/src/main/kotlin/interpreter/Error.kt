package interpreter

import domain.Error

enum class RuntimeError(
    val reason: String,
) : Error {
    VARIABLE_ALREADY_DEFINED("Variable already defined"),
    VARIABLE_DOESNT_EXIST("Variable doesn't exist"),
    MATH_ERROR("cannot resolve operation"),
    VARIABLE_HAS_DIFFERENT_TYPE("That variable exists with a different type"),
    STRING_CONCATENATION_NEEDS_INT("to concat strings, integer are required"),
    ;

    override fun getMessage(): String = reason
}
