package formatter

import formatter.formatrules.EnsureNoSpaceAroundEquals
import formatter.formatrules.EnsureSingleSpace
import formatter.formatrules.EnsureSpaceAfterColon
import formatter.formatrules.EnsureSpaceAroundEquals
import formatter.formatrules.EnsureSpaceBeforeColon
import formatter.formatrules.EnsureSpacesSurroundingOperations
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

data class ConfigProvider(
    val ruleSet: Map<FormatTokenizer, FormatRules>,
) {
    companion object {
        fun defaultFor(version: String = "1.0"): ConfigProvider = if (version == "1.1") default11() else default10()

        fun default10(): ConfigProvider {
            val defaultRules =
                FormatRules(
                    listOf(
                        EnsureSpacesSurroundingOperations(false),
                        EnsureSingleSpace(false),
                        EnsureSpaceAroundEquals(false),
                        EnsureNoSpaceAroundEquals(false),
                        EnsureSpaceBeforeColon(false),
                        EnsureSpaceAfterColon(false),
                        LineBreaksAfterPrintLn(0),
                    ),
                )
            return ConfigProvider(
                mapOf(
                    DeclarationFormatTokenizer() to defaultRules,
                    AssignmentFormatTokenizer() to defaultRules,
                    ExpressionFormatTokenizer() to defaultRules,
                ),
            )
        }

        fun default11(): ConfigProvider {
            val defaultRules =
                FormatRules(
                    listOf(
                        EnsureSpacesSurroundingOperations(false),
                        EnsureSingleSpace(false),
                        EnsureSpaceAroundEquals(false),
                        EnsureNoSpaceAroundEquals(false),
                        EnsureSpaceBeforeColon(false),
                        EnsureSpaceAfterColon(false),
                        IfBraceSameLine(false),
                        IfBraceBelowLine(false),
                        IndentsInsideIf(0),
                        LineBreaksAfterPrintLn(0),
                    ),
                )
            val stmts =
                mutableSetOf<FormatTokenizer>(
                    DeclarationFormatTokenizer(),
                    AssignmentFormatTokenizer(),
                    ExpressionFormatTokenizer(),
                )
            val conditionalTokenizer = ConditionalFormatTokenizer(stmts)
            stmts.add(conditionalTokenizer)

            return ConfigProvider(
                mapOf(
                    DeclarationFormatTokenizer() to defaultRules,
                    AssignmentFormatTokenizer() to defaultRules,
                    ExpressionFormatTokenizer() to defaultRules,
                    conditionalTokenizer to defaultRules,
                ),
            )
        }
    }
}
