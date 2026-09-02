package interpreter

import domain.Error

enum class RuntimeError(
    val reason: String,
) : Error {
    VARIABLE_ALREADY_DEFINED("Variable already defined") {
        override fun getMessage() = reason
    },
    VARIABLE_DOESNT_EXIST("Variable doesn't exist") {
        override fun getMessage() = reason
    },
    MATH_ERROR("cannot resolve operation") {
        override fun getMessage() = reason
    },
    VARIABLE_HAS_DIFFERENT_TYPE("That variable exists with a different type") {
        override fun getMessage() = reason
    },
}
