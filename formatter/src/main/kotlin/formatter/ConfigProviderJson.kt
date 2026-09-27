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
private data class FormatterConfigPatch(
    @SerialName("enforce-spacing-around-equals") val enforceSpacingAroundEquals: Boolean? = null,
    @SerialName("enforce-no-spacing-around-equals") val enforceNoSpacingAroundEquals: Boolean? = null,
    @SerialName("enforce-spacing-before-colon-in-declaration") val enforceSpacingBeforeColon: Boolean? = null,
    @SerialName("enforce-spacing-after-colon-in-declaration") val enforceSpacingAfterColon: Boolean? = null,
    @SerialName("mandatory-single-space-separation") val enforceSingleSpaces: Boolean? = null,
    @SerialName("mandatory-space-surrounding-operations") val enforceSpacingSurroundingOperations: Boolean? = null,
    @SerialName("if-brace-same-line") val ifBraceSameLine: Boolean? = null,
    @SerialName("if-brace-below-line") val ifBraceBelowLine: Boolean? = null,
    @SerialName("indent-inside-if") val indentInsideIf: Int? = null,
    @SerialName("line-breaks-after-println") val lineBreaksAfterPrintln: Int? = null,
)

private val formatterConfigJson = Json { ignoreUnknownKeys = true }

internal fun resolveFormatterConfig(
    version: String,
    configJson: String?,
): Either<Error, ConfigProvider> =
    resolveConfigPatch(
        default = ConfigProvider.defaultFor(version),
        configJson = configJson,
    )

private fun resolveConfigPatch(
    default: ConfigProvider,
    configJson: String?,
): Either<Error, ConfigProvider> {
    if (configJson == null) return Success(default)

    val patch =
        try {
            // toma la instancia de json (que permite unknown keys) y decodifica la config
            // usando el FormatterConfigPatch
            formatterConfigJson.decodeFromString<FormatterConfigPatch>(configJson)
        } catch (_: SerializationException) {
            return Failure(FormattingError.INVALID_JSON)
        }
    validateConfigPatch(patch)?.let { return Failure(it) }

    val mergedRuleSet =
        default.ruleSet.mapValues { (_, defaultRules) ->
            FormatRules(
                defaultRules.list.map { defaultRule ->
                    mergeRule(defaultRule, patch)
                },
            )
        }

    return Success(ConfigProvider(mergedRuleSet))
}

private fun validateConfigPatch(patch: FormatterConfigPatch): FormatterConfigError? {
    if (patch.enforceSpacingAroundEquals == true && patch.enforceNoSpacingAroundEquals == true) {
        return FormatterConfigError(
            "Options 'enforce-spacing-around-equals' and 'enforce-no-spacing-around-equals' cannot both be enabled",
        )
    }
    if (patch.ifBraceSameLine == true && patch.ifBraceBelowLine == true) {
        return FormatterConfigError("Options 'if-brace-same-line' and 'if-brace-below-line' cannot both be enabled")
    }
    if (patch.indentInsideIf != null && patch.indentInsideIf < 0) {
        return FormatterConfigError("Option 'indent-inside-if' must be greater than or equal to 0")
    }
    if (patch.lineBreaksAfterPrintln != null && patch.lineBreaksAfterPrintln < 0) {
        return FormatterConfigError("Option 'line-breaks-after-println' must be greater than or equal to 0")
    }

    return null
}

private fun mergeRule(
    defaultRule: FormatRule,
    patch: FormatterConfigPatch,
): FormatRule =
    when (defaultRule) {
        is EnsureSpacesSurroundingOperations ->
            patch.enforceSpacingSurroundingOperations?.let(::EnsureSpacesSurroundingOperations) ?: defaultRule
        is EnsureSingleSpace -> patch.enforceSingleSpaces?.let(::EnsureSingleSpace) ?: defaultRule
        is EnsureSpaceAroundEquals -> patch.enforceSpacingAroundEquals?.let(::EnsureSpaceAroundEquals) ?: defaultRule
        is EnsureNoSpaceAroundEquals -> patch.enforceNoSpacingAroundEquals?.let(::EnsureNoSpaceAroundEquals) ?: defaultRule
        is EnsureSpaceBeforeColon -> patch.enforceSpacingBeforeColon?.let(::EnsureSpaceBeforeColon) ?: defaultRule
        is EnsureSpaceAfterColon -> patch.enforceSpacingAfterColon?.let(::EnsureSpaceAfterColon) ?: defaultRule
        is IfBraceSameLine -> patch.ifBraceSameLine?.let(::IfBraceSameLine) ?: defaultRule
        is IfBraceBelowLine -> patch.ifBraceBelowLine?.let(::IfBraceBelowLine) ?: defaultRule
        is IndentsInsideIf -> patch.indentInsideIf?.let(::IndentsInsideIf) ?: defaultRule
        is LineBreaksAfterPrintLn -> patch.lineBreaksAfterPrintln?.let(::LineBreaksAfterPrintLn) ?: defaultRule
        else -> defaultRule
    }

@Deprecated("Use Formatter.format(source, version, configJson) instead")
fun applyJsonConfig(
    default: ConfigProvider,
    json: String,
): Either<Error, ConfigProvider> = resolveConfigPatch(default, configJson = json)

@Deprecated("Read the file outside the formatter and call Formatter.format(source, version, configJson) instead")
fun applyJsonConfig(
    default: ConfigProvider,
    path: File,
): Either<Error, ConfigProvider> {
    val json =
        try {
            path.readText()
        } catch (_: IOException) {
            return Failure(FormattingError.FILE_NOT_FOUND)
        }
    return resolveConfigPatch(default, configJson = json)
}
