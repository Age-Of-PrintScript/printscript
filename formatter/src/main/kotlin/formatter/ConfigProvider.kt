package formatter

import formatter.formatrules.FormatRules
import formatter.formattokens.FormatTokenizer

data class ConfigProvider(
    val ruleSet: Map<FormatTokenizer, FormatRules>,
)
