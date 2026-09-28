package formatter

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
import formatter.formattokens.AssignmentFormatTokenizer
import formatter.formattokens.ConditionalFormatTokenizer
import formatter.formattokens.DeclarationFormatTokenizer
import formatter.formattokens.ExpressionFormatTokenizer
import formatter.formattokens.FormatTokenizer
import versionfactory.PSVersion
import versionfactory.version1_0
import versionfactory.version1_1

internal data class FormattingProfile(
    val tokenizers: List<FormatTokenizer>,
    val defaultRules: FormatRules,
)

internal data class ResolvedFormatterConfig(
    val tokenizers: List<FormatTokenizer>,
    val rules: FormatRules,
)

internal fun formattingProfileFor(version: PSVersion): FormattingProfile =
    when (version) {
        version1_0 -> FormattingProfile(version10Tokenizers(), defaultRules(includeConditionalRules = false))
        version1_1 -> FormattingProfile(version11Tokenizers(), defaultRules(includeConditionalRules = true))
        else -> error("Unsupported PrintScript version")
    }

private fun version10Tokenizers(): List<FormatTokenizer> =
    listOf(
        DeclarationFormatTokenizer(),
        AssignmentFormatTokenizer(),
        ExpressionFormatTokenizer(),
    )

private fun version11Tokenizers(): List<FormatTokenizer> {
    val simpleTokenizers = version10Tokenizers()
    lateinit var tokenizers: List<FormatTokenizer>
    val conditionalTokenizer = ConditionalFormatTokenizer { tokenizers }
    tokenizers = simpleTokenizers + conditionalTokenizer
    return tokenizers
}

private fun defaultRules(includeConditionalRules: Boolean): FormatRules =
    FormatRules(
        buildList<FormatRule> {
            add(EnsureSpacesSurroundingOperations(false))
            add(EnsureSingleSpace(false))
            add(EnsureSpaceAroundEquals(false))
            add(EnsureNoSpaceAroundEquals(false))
            add(EnsureSpaceBeforeColon(false))
            add(EnsureSpaceAfterColon(false))
            if (includeConditionalRules) {
                add(IfBraceSameLine(false))
                add(IfBraceBelowLine(false))
                add(IndentsInsideIf(0))
            }
            add(LineBreaksAfterPrintLn(0))
        },
    )
