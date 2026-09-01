package interpreter.cases.success

import ast.Program
import interpreter.SuccessCase
import interpreter.cases.*

val SUCCESS_CASES =
    listOf(
        SuccessCase(
            name = "declaration with number initialization",
            program = Program(listOf(DECLARATION_X_NUMBER_5), POS, POS),
            expectedEnv = ENV_WITH_X_EQUAL_TO_5,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration with string initialization",
            program = Program(listOf(DECLARATION_X_STRING_HOLA), POS, POS),
            expectedEnv = ENV_WITH_X_STRING_HOLA,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration without initialization (number)",
            program = Program(listOf(DECLARATION_X_NUMBER_NO_VALUE), POS, POS),
            expectedEnv = ENV_WITH_X_NUMBER_NO_VALUE,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration without initialization (string)",
            program = Program(listOf(DECLARATION_X_STRING_NO_VALUE), POS, POS),
            expectedEnv = ENV_WITH_X_STRING_NO_VALUE,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration without value, then valid assignment",
            program = Program(listOf(DECLARATION_X_NUMBER_NO_VALUE, ASSIGNMENT_X_TO_10), POS, POS),
            expectedEnv = ENV_WITH_X_EQUAL_TO_10,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration with value, then reassignment",
            program = Program(listOf(DECLARATION_X_NUMBER_5, ASSIGNMENT_X_TO_2), POS, POS),
            expectedEnv = ENV_WITH_X_EQUAL_TO_2,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "println with string literal",
            program = Program(listOf(CALL_PRINTLN_HOLA), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_HOLA,
        ),
        SuccessCase(
            name = "println with declared variable",
            program = Program(listOf(DECLARATION_X_NUMBER_5, CALL_PRINTLN_X), POS, POS),
            expectedEnv = ENV_WITH_X_EQUAL_TO_5,
            expectedEvents = EVENTS_WITH_PRINT_5,
        ),
        SuccessCase(
            name = "println with arithmetic operation",
            program = Program(listOf(CALL_PRINTLN_OPERATION_1_PLUS_2), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_3,
        ),
        SuccessCase(
            name = "declaration, reassignment and println combined",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_5, ASSIGNMENT_X_TO_10, CALL_PRINTLN_X),
                    POS, POS,
                ),
            expectedEnv = ENV_WITH_X_EQUAL_TO_10,
            expectedEvents = EVENTS_WITH_PRINT_10,
        ),
        SuccessCase(
            name = "two independent variables declared",
            program = Program(listOf(DECLARATION_X_NUMBER_5, DECLARATION_A_NUMBER_1), POS, POS),
            expectedEnv = ENV_WITH_X_5_AND_A_1,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "declaration initializing variable with another declared variable",
            program = Program(listOf(DECLARATION_X_NUMBER_5, DECLARATION_Y_NUMBER_WITH_X_VALUE), POS, POS),
            expectedEnv = ENV_WITH_X_5_AND_Y_5,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "assignment to variable using another declared variable",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_5, DECLARATION_Y_NUMBER_NO_VALUE, ASSIGNMENT_Y_TO_X),
                    POS, POS,
                ),
            expectedEnv = ENV_WITH_X_5_AND_Y_5,
            expectedEvents = EMPTY_EVENTS,
        ),
        SuccessCase(
            name = "multiple println calls in sequence",
            program = Program(listOf(CALL_PRINTLN_HOLA, CALL_PRINTLN_MUNDO), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_HOLA_THEN_MUNDO,
        ),
        SuccessCase(
            name = "println with string plus number concatenation",
            program = Program(listOf(CALL_PRINTLN_STRING_PLUS_NUMBER), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_HOLA_5,
        ),
        SuccessCase(
            name = "println with number plus string concatenation",
            program = Program(listOf(CALL_PRINTLN_NUMBER_PLUS_STRING), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_5_HOLA,
        ),
        SuccessCase(
            name = "println with string plus string concatenation",
            program = Program(listOf(CALL_PRINTLN_STRING_PLUS_STRING), POS, POS),
            expectedEnv = EMPTY_ENV,
            expectedEvents = EVENTS_WITH_PRINT_HOLA_MUNDO,
        ),
        SuccessCase(
            name = "multiple println calls showing variable state before and after reassignment",
            program =
                Program(
                    listOf(DECLARATION_X_NUMBER_5, CALL_PRINTLN_X, ASSIGNMENT_X_TO_10, CALL_PRINTLN_X),
                    POS, POS,
                ),
            expectedEnv = ENV_WITH_X_EQUAL_TO_10,
            expectedEvents = EVENTS_WITH_PRINT_5_THEN_10,
        ),
    )
