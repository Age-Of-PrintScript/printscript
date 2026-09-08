package interpreter

import domain.Error

enum class RuntimeError(
    val reason: String,
) : Error {
    VARIABLE_ALREADY_DEFINED("Variable already defined"),
    VARIABLE_DOESNT_EXIST("Variable doesn't exist"),
    MATH_ERROR("cannot resolve operation"),
    VARIABLE_HAS_DIFFERENT_TYPE("That variable exists with a different type"),
    STRING_REPETITION_REQUIRES_INT("string repetition requires a positive integer"),
    MISSING_ASSIGNATION("Variable cannot be assigned to void"),
    MISSING_EVALUATOR_FOR_AST("AST evaluator not found")
    ;

    override fun getMessage(): String = reason
}
