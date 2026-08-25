package formatter.astformatterfactory

import formatter.ASTFormatter
import formatter.FormatterImplementation
import formatter.formatrules.FormatRule
import formatter.formatrules.FormattingRules
import formatter.formatrules.LineBeforeDeclarationRule
import formatter.formatrules.SpaceAfterColonRule
import formatter.formatrules.SpaceBeforeColonRule
import kotlinx.serialization.json.JsonObject
import java.text.Format

class FormatFactory(val json: JsonObject) {

    fun create():List<ASTFormatter> {

        val declarationFormatter = FormatterImplementation(
            FormattingRules(
                listOf(
                    SpaceBeforeColonRule(),
                    SpaceAfterColonRule(),
                )
            )
        )
        val assignmentFormatter = FormatterImplementation(
            FormattingRules(
                listOf(
                    SpaceBeforeColonRule(),
                )
            )
        )
        val callFormatter = FormatterImplementation(
            FormattingRules(
                listOf(
                    LineBeforeDeclarationRule(),
                )
            )
        )
        return listOf(declarationFormatter, assignmentFormatter, callFormatter)
    }
}