package interpreter.cases.success

import ast.Program
import interpreter.cases.*
import interpreter.cases.SuccessCase

val SUCCESS_CASES = listOf(
    SuccessCase(
        name = "declaration with number initialization",
        program = Program(listOf(DECLARATION_X_NUMBER_5), POS, POS),
        expectedEnv = ENV_WITH_X_EQUAL_TO_5,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "declaration with string initialization",
        program = Program(listOf(DECLARATION_X_STRING_HOLA), POS, POS),
        expectedEnv = ENV_WITH_X_STRING_HOLA,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "declaration without initialization (number)",
        program = Program(listOf(DECLARATION_X_NUMBER_NO_VALUE), POS, POS),
        expectedEnv = ENV_WITH_X_NUMBER_NO_VALUE,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "declaration without initialization (string)",
        program = Program(listOf(DECLARATION_X_STRING_NO_VALUE), POS, POS),
        expectedEnv = ENV_WITH_X_STRING_NO_VALUE,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "declaration without value, then valid assignment",
        program = Program(listOf(DECLARATION_X_NUMBER_NO_VALUE, ASSIGNMENT_X_TO_10), POS, POS),
        expectedEnv = ENV_WITH_X_EQUAL_TO_10,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "declaration with value, then reassignment",
        program = Program(listOf(DECLARATION_X_NUMBER_5, ASSIGNMENT_X_TO_2), POS, POS),
        expectedEnv = ENV_WITH_X_EQUAL_TO_2,
        expectedEvents = EMPTY_EVENTS
    ),

    SuccessCase(
        name = "println with string literal",
        program = Program(listOf(CALL_PRINTLN_HOLA), POS, POS),
        expectedEnv = EMPTY_ENV,
        expectedEvents = EVENTS_WITH_PRINT_HOLA
    ),

    SuccessCase(
        name = "println with declared variable",
        program = Program(listOf(DECLARATION_X_NUMBER_5, CALL_PRINTLN_X), POS, POS),
        expectedEnv = ENV_WITH_X_EQUAL_TO_5,
        expectedEvents = EVENTS_WITH_PRINT_5
    ),

    SuccessCase(
        name = "println with arithmetic operation",
        program = Program(listOf(CALL_PRINTLN_OPERATION_1_PLUS_2), POS, POS),
        expectedEnv = EMPTY_ENV,
        expectedEvents = EVENTS_WITH_PRINT_3
    ),

    SuccessCase(
        name = "declaration, reassignment and println combined",
        program = Program(
            listOf(DECLARATION_X_NUMBER_5, ASSIGNMENT_X_TO_10, CALL_PRINTLN_X),
            POS, POS
        ),
        expectedEnv = ENV_WITH_X_EQUAL_TO_10,
        expectedEvents = EVENTS_WITH_PRINT_5_THEN_10.let {
            // ojo: println se ejecuta después de la reasignación a 10,
            // por lo que el evento esperado es solo "10", no "5" y "10".
            interpreter.RuntimeEvents(listOf(it.events.last()))
        }
    ),

    SuccessCase(
        name = "two independent variables declared",
        program = Program(listOf(DECLARATION_X_NUMBER_5, DECLARATION_A_NUMBER_1), POS, POS),
        expectedEnv = interpreter.RuntimeEnvironment(
            ENV_WITH_X_EQUAL_TO_5.variableMap + ENV_WITH_A_EQUAL_TO_1.variableMap
        ),
        expectedEvents = EMPTY_EVENTS
    ),
)
