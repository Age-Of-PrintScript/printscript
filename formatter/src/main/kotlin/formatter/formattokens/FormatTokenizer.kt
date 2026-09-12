package formatter.formattokens

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import formatter.FormattingError

interface FormatTokenizer {
    fun tokenize(ast: AST): Either<FormattingError, FormatTokens>
}

class DeclarationFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.DeclarationStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        return Success(
            FormatTokens(
                listOf(
                    Text("let"),
                    WhiteSpace,
                    Text(ast.id), // hay que armar el utils que traduce a string
                    Text(":"),
                    Text("${ast.type}:"),
                    Text("="),
                    Text("${ast.value}:"),
                    Text(";"),
                    EOL,
                ),
            ),
        )
    }
}

class AssignmentFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.AssignmentStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        return Success(
            FormatTokens(
                listOf(
                    Text(ast.id),
                    Text("="),
                    Text("${ast.value}:"),
                    Text(";"),
                    EOL,
                ),
            ),
        )
    }
}

class ExpressionFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.ExpressionStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }

        return Success(
            FormatTokens(
                listOf(
                    Text("${ast.expression}"), // hay que armar el utils que traduce a string
                    EOL,
                ),
            ),
        )
    }
}
