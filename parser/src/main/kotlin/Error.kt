
interface ParsingError: Error
data class NO_ASSIGNMENT_ERROR(val reason: String = "Expression expected"): ParsingError
data class ILLEGAL_OPERATION(val reason: String = "Illegal arithmetic operation"): ParsingError
data class SINTAX_ERROR(val mensaje: String = "Sintax Error"): ParsingError

