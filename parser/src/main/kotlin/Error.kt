
enum class ParsingError(val mensaje: String): Error {
    NO_ASSIGNMENT_ERROR("Expression expected"),
    ILLEGAL_OPERATION("Illegal arithmetic operation");
    //TODO -> AGREGAR LOS TIPOS CORRESPONDIENTES DE ERRORES
}
