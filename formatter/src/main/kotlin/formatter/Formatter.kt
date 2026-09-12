package formatter

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import formatter.formatrules.FormatRules
import formatter.formattokens.FormatTokenizer
import formatter.stringifier.stringify

internal interface ASTFormatter {
    fun format(ast: AST): Either<FormattingError, String>
}

internal class FormatterImplementation(
    val rules: FormatRules,
    val tokenizer: FormatTokenizer,
) : ASTFormatter {
    override fun format(ast: AST): Either<FormattingError, String> {
        val tokenized = tokenizer.tokenize(ast).getOrReturn { return Failure(it) }
        var tokens = tokenized

        for (rule in rules.list) {
            tokens = rule.apply(tokens)
        }

        val finalString = stringify(tokens).replace("\n", System.lineSeparator())
        return Success(finalString)
    }
}
