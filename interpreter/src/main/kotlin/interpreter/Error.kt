package interpreter

import domain.Error

enum class RuntimeError(val reason: String): Error {
    VARIABLE_ALREADY_DEFINED("Variable already defined"),
    VARIABLE_DOESNT_EXIST("Variable doesn't exist"),
    VARIABLE_HAS_DIFFERENT_TYPE("That variable exist with a different type");
}