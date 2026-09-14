package formatter.stringifier

import formatter.formattokens.EOL
import formatter.formattokens.FormatToken
import formatter.formattokens.FormatTokens
import formatter.formattokens.Indent
import formatter.formattokens.Text
import formatter.formattokens.WhiteSpace

fun stringify(tokens: FormatTokens): String {
    var result = ""
    for (token in tokens.list) {
        result += translateToken(token)
    }
    return result
}

fun translateToken(token: FormatToken): String =
    when (token) {
        is Text -> token.value
        is WhiteSpace -> " "
        is EOL -> "\n"
        is Indent -> "\t"
    }
