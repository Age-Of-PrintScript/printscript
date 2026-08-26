package interpreter.cases.failure

import ast.Program
import interpreter.FailureCase
import interpreter.RuntimeError
import interpreter.cases.*

val FAILURE_CASES =
    listOf(
        FailureCase(
            name = "redeclaration of already defined variable",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_5, DECLARATION_X_STRING_HOLA),
                    POS, POS,
                ),
            expectedFailure = RuntimeError.VARIABLE_ALREADY_DEFINED,
        ),
        FailureCase(
            name = "assignment to undeclared variable",
            program = Program(listOf(ASSIGNMENT_UNDECLARED_A_TO_5), POS, POS),
            expectedFailure = RuntimeError.VARIABLE_DOESNT_EXIST,
        ),
        FailureCase(
            name = "assignment with mismatched type (number reassigned to string)",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_5, ASSIGNMENT_X_TO_STRING_X),
                    POS, POS,
                ),
            expectedFailure = RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE,
        ),
        FailureCase(
            name = "declaration with value of mismatched type",
            program = Program(listOf(DECLARATION_X_NUMBER_WITH_STRING_VALUE), POS, POS),
            expectedFailure = RuntimeError.VARIABLE_HAS_DIFFERENT_TYPE,
        ),
        FailureCase(
            name = "declaration with invalid operation in initializer",
            program = Program(listOf(DECLARATION_X_NUMBER_WITH_INVALID_OPERATION), POS, POS),
            expectedFailure = RuntimeError.MATH_ERROR,
        ),
        FailureCase(
            name = "println with undeclared variable",
            program = Program(listOf(CALL_PRINTLN_UNDECLARED_A), POS, POS),
            expectedFailure = RuntimeError.MATH_ERROR,
        ),
        FailureCase(
            name = "println with uninitialized variable used in operation",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_NO_VALUE, CALL_PRINTLN_OPERATION_X_PLUS_1),
                    POS, POS,
                ),
            expectedFailure = RuntimeError.MATH_ERROR,
        ),
        FailureCase(
            name = "declaration initializing variable with uninitialized variable",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_NO_VALUE, DECLARATION_Y_NUMBER_WITH_X_VALUE),
                    POS, POS,
                ),
            expectedFailure = RuntimeError.MATH_ERROR,
        ),
        FailureCase(
            name = "assignment to variable using uninitialized variable",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_NO_VALUE, DECLARATION_Y_NUMBER_NO_VALUE, ASSIGNMENT_Y_TO_X),
                    POS, POS,
                ),
            expectedFailure = RuntimeError.MATH_ERROR,
        ),
    )
