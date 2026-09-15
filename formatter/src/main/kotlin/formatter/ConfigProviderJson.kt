package formatter

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import formatter.formatrules.EnsureNoSpaceAroundEquals
import formatter.formatrules.EnsureSingleSpace
import formatter.formatrules.EnsureSpaceAfterColon
import formatter.formatrules.EnsureSpaceAroundEquals
import formatter.formatrules.EnsureSpaceBeforeColon
import formatter.formatrules.EnsureSpacesSurroundingOperations
import formatter.formatrules.FormatRule
import formatter.formatrules.FormatRules
import formatter.formatrules.IfBraceBelowLine
import formatter.formatrules.IfBraceSameLine
import formatter.formatrules.IndentsInsideIf
import formatter.formatrules.LineBreaksAfterPrintLn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

@Serializable
private data class RawFormatterConfig(
    @SerialName("enforce-spacing-around-equals") val enforceSpacingAroundEquals: Boolean = false,
    @SerialName("enforce-no-spacing-around-equals") val enforceNoSpacingAroundEquals: Boolean = false,
    @SerialName("enforce-spacing-before-colon-in-declaration") val enforceSpacingBeforeColon: Boolean = false,
    @SerialName("enforce-spacing-after-colon-in-declaration") val enforceSpacingAfterColon: Boolean = false,
    @SerialName("mandatory-single-space-separation") val enforceSingleSpaces: Boolean = false,
    @SerialName("mandatory-space-surrounding-operations") val enforceSpacingSurroundingOperations: Boolean = false,
    @SerialName("if-brace-same-line") val ifBraceSameLine: Boolean = false,
    @SerialName("if-brace-below-line") val ifBraceBelowLine: Boolean = false,
    @SerialName("indent-inside-if") val indentInsideIf: Int = 0,
    @SerialName("line-breaks-after-println") val lineBreaksAfterPrintln: Int = 0,
)

private val configJson = Json { ignoreUnknownKeys = true } // mismo estilo que linter/ConfigParser.kt

// lo ausente queda en su valor "apagado" (false/0).
private fun formatRulesFromJson(json: String): FormatRules {
    val raw = configJson.decodeFromString<RawFormatterConfig>(json)
    return FormatRules(
        listOf(
            EnsureSpacesSurroundingOperations(raw.enforceSpacingSurroundingOperations),
            EnsureSingleSpace(raw.enforceSingleSpaces),
            EnsureSpaceAroundEquals(raw.enforceSpacingAroundEquals),
            EnsureNoSpaceAroundEquals(raw.enforceNoSpacingAroundEquals),
            EnsureSpaceBeforeColon(raw.enforceSpacingBeforeColon),
            EnsureSpaceAfterColon(raw.enforceSpacingAfterColon),
            IfBraceSameLine(raw.ifBraceSameLine),
            IfBraceBelowLine(raw.ifBraceBelowLine),
            IndentsInsideIf(raw.indentInsideIf),
            LineBreaksAfterPrintLn(raw.lineBreaksAfterPrintln),
        ),
    )
}

// "Activada": para las reglas booleanas es su propio flag; para las numéricas (sin
// on/off propio) se toma "distinto del default 0" como activada. Como el default
// del engine para esas reglas ya es 0, un json que también diga 0 (caso real:
// line-breaks-after-println: 0) da el mismo resultado se reemplace o no.
private fun isActivated(rule: FormatRule): Boolean =
    when (rule) {
        is EnsureSpaceAroundEquals -> rule.activated
        is EnsureNoSpaceAroundEquals -> rule.activated
        is EnsureSpaceBeforeColon -> rule.activated
        is EnsureSpaceAfterColon -> rule.activated
        is IfBraceSameLine -> rule.activated
        is IfBraceBelowLine -> rule.activated
        is IndentsInsideIf -> rule.indents != 0
        is LineBreaksAfterPrintLn -> rule.lines.toInt() != 0
        is EnsureSpacesSurroundingOperations -> rule.activated
        is EnsureSingleSpace -> rule.activated
        else -> false
    }

// json; si esa contraparte está activada la usa, si no deja la default tal cual.
// Si el archivo no existe/no se puede leer, o el json es inválido, se devuelve el
// error correspondiente en vez de dejar propagar la excepción cruda.
internal fun applyJsonConfig(
    default: ConfigProvider,
    path: File,
): Either<Error, ConfigProvider> {
    val json =
        try {
            path.readText()
        } catch (e: IOException) {
            System.err.println("No se pudo leer el archivo de configuración '$path': ${e.message}")
            return Failure(FormattingError.FILE_NOT_FOUND)
        }

    val parsedRules =
        try {
            formatRulesFromJson(json)
        } catch (e: SerializationException) {
            System.err.println("El json de configuración es inválido: ${e.message}")
            return Failure(FormattingError.INVALID_JSON)
        }

    fun replaceToRulesFromJson(
        defaultRules: FormatRules,
        parsedRules: FormatRules,
    ): FormatRules =
        FormatRules(
            defaultRules.list.map { defaultRule ->
                val fromJson = parsedRules.list.firstOrNull { it::class == defaultRule::class }
                // no puedo desacoplar pq el metodo necesita saber si esta activado
                if (fromJson != null && isActivated(fromJson)) fromJson else defaultRule
            },
        )

    val configRules = replaceToRulesFromJson(default.rules, parsedRules)

    return Success(ConfigProvider(default.tokenizers, configRules))
}
