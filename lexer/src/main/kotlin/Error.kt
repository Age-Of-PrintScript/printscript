import domain.Error

enum class LexerError(val mensaje: String): Error {
    LEXICAL_ERROR("Unresolved reference"),;
    //TODO -> AGREGAR LOS TIPOS CORRESPONDIENTES DE ERRORES
}
