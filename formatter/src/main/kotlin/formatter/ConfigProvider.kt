package formatter

import domain.Either
import domain.Failure
import domain.Success
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
import formatter.formattokens.FormatTokenizers

internal data class ConfigProvider(
    val tokenizers: FormatTokenizers,
    val rules: FormatRules,
) {
    companion object {
        fun defaultFor(psVersion: String): Either<FormattingError, ConfigProvider> =
            when (psVersion) {
                "1.0" -> Success(createConfig1Point0())
                "1.1" -> Success(createConfig1Point1())
                else -> Failure(FormattingError.INVALID_VERSION)
            }

        // Reglas con gate "activated" (EnsureNoSpaceAroundEquals, EnsureSpaceBeforeColon, IfBraceSameLine,
        // IfBraceBelowLine) quedan en false: es su estado neutro real, tal como espera applyJsonConfig (lo
        // que el json del usuario no active queda "apagado"). IndentsInsideIf y LineBreaksAfterPrintLn no
        // tienen ese gate, se aplican siempre (0 no es "apagado", es un valor real), por eso su default no
        // puede ser cualquiera: LineBreaksAfterPrintLn(0) da la separación normal de una línea por statement,
        // e IndentsInsideIf(2) dos espacios por nivel de indentación. EnsureSpaceAroundEquals / EnsureSpaceAfterColon
        // sí tienen gate, pero van activas por defecto (estilo canónico: "let x: type = value;").

        private fun defaultFormatRules(): FormatRules =
            FormatRules(
                listOf(
                    EnsureSpaceAroundEquals(true),
                    EnsureNoSpaceAroundEquals(false),
                    EnsureSpaceBeforeColon(false),
                    EnsureSpaceAfterColon(true),
                    EnsureSpacesSurroundingOperations(false),
                    EnsureSingleSpace(false),
                    IfBraceSameLine(false),
                    IfBraceBelowLine(false),
                    IndentsInsideIf(2),
                    LineBreaksAfterPrintLn(0),
                ),
            )

        private fun createConfig1Point0(): ConfigProvider {
            val declarationTokenizer = DeclarationFormatTokenizer()
            val assignmentTokenizer = AssignmentFormatTokenizer()
            val expressionTokenizer = ExpressionFormatTokenizer()

            return ConfigProvider(
                FormatTokenizers(listOf(declarationTokenizer, assignmentTokenizer, expressionTokenizer)),
                defaultFormatRules(),
            )
        }

        private fun createConfig1Point1(): ConfigProvider {
            val declarationTokenizer = DeclarationFormatTokenizer()
            val assignmentTokenizer = AssignmentFormatTokenizer()
            val expressionTokenizer = ExpressionFormatTokenizer()

            val statementFormatters: MutableSet<FormatTokenizer> =
                mutableSetOf(declarationTokenizer, assignmentTokenizer, expressionTokenizer)
            val conditionalTokenizer = ConditionalFormatTokenizer(statementFormatters)
            statementFormatters.add(conditionalTokenizer)

            return ConfigProvider(
                FormatTokenizers(
                    listOf(declarationTokenizer, assignmentTokenizer, expressionTokenizer, conditionalTokenizer),
                ),
                defaultFormatRules(),
            )
        }
    }
}
