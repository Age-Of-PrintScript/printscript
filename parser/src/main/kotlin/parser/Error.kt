package parser

import domain.Error

interface ParsingError: Error
data class NO_ASSIGNMENT_ERROR(val reason: String = "Expression expected"): ParsingError
data class ILLEGAL_OPERATION(val reason: String = "Illegal arithmetic operation"): ParsingError
data class SYNTAX_ERROR(val mensaje: String = "Syntax Error"): ParsingError

