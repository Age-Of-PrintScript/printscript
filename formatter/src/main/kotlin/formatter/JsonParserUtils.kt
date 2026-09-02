package formatter

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.io.FileNotFoundException

fun parseConfig(path: String): Either<Error, JsonObject> {
    val configText =
        try {
            File(path).readText()
        } catch (e: FileNotFoundException) {
            System.err.println("No se pudo leer el archivo de config: ${e.message}")
            return Failure(FormattingError.FILE_NOT_FOUND)
        }

    return try {
        val json = Json.parseToJsonElement(configText).jsonObject
        Success(json)
    } catch (e: SerializationException) {
        System.err.println("No se pudo serializar: ${e.message}")
        Failure(FormattingError.INVALID_JSON)
    }
}
