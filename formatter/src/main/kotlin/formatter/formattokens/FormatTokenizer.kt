package formatter.formattokens

import ast.AST
import ast.Block
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import formatter.FormattingError

internal interface FormatTokenizer {
    fun tokenize(ast: AST): Either<FormattingError, FormatTokens>
}

// 1.0

internal class DeclarationFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.DeclarationStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        val valueTokens =
            ast.value?.let { listOf(Text("=")) + expressionToFormatTokens(it) } ?: emptyList()
        return Success(
            FormatTokens(
                listOf(
                    Text("let"),
                    WhiteSpace,
                    Text(ast.id),
                    Text(":"),
                    Text(ast.type.name),
                ) + valueTokens + listOf(Text(";"), EOL),
            ),
        )
    }
}

internal class AssignmentFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.AssignmentStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        return Success(
            FormatTokens(
                listOf(Text(ast.id), Text("=")) +
                    expressionToFormatTokens(ast.value) +
                    listOf(Text(";"), EOL),
            ),
        )
    }
}

internal class ExpressionFormatTokenizer : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.ExpressionStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }

        return Success(
            FormatTokens(
                expressionToFormatTokens(ast.expression) + listOf(Text(";"), EOL),
            ),
        )
    }
}

// 1.1

internal class ConditionalFormatTokenizer(
    private val statementFormatters: Set<FormatTokenizer>,
) : FormatTokenizer {
    override fun tokenize(ast: AST) = tokenizeAtDepth(ast, 1)

    private fun tokenizeAtDepth(
        ast: AST,
        depth: Int,
    ): Either<FormattingError, FormatTokens> {
        if (ast !is AST.ConditionalStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        val conditionTokens = expressionToFormatTokens(ast.condition)
        val thenTokens = tokenizeBlock(ast.ifBlock, depth).getOrReturn { return Failure(it) }
        val elseTokens =
            ast.elseBlock?.let { elseBlock ->
                tokenizeBlock(elseBlock, depth).getOrReturn { error -> return Failure(error) }
            }

        val header =
            listOf(Text("if"), WhiteSpace, Text("(")) + conditionTokens + listOf(Text(")"), WhiteSpace, Text("{"), EOL)
        val closingIndent = List(depth - 1) { Indent }
        val tail =
            elseTokens?.let {
                closingIndent + listOf(Text("}"), WhiteSpace, Text("else"), WhiteSpace, Text("{"), EOL) +
                    it.list + closingIndent + listOf(Text("}"), EOL)
            } ?: (closingIndent + listOf(Text("}"), EOL))

        return Success(FormatTokens(header + thenTokens.list + tail))
    }

    private fun tokenizeBlock(
        block: Block,
        depth: Int,
    ): Either<FormattingError, FormatTokens> {
        val tokens = mutableListOf<FormatToken>()
        for (statement in block.statements) {
            val tokenizer =
                statementFormatters.firstOrNull { it.tokenize(statement) is Success }
                    ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)

            val statementTokens =
                if (tokenizer is ConditionalFormatTokenizer) {
                    tokenizer.tokenizeAtDepth(statement, depth + 1).getOrReturn { return Failure(it) }
                } else {
                    tokenizer.tokenize(statement).getOrReturn { return Failure(it) }
                }

            repeat(depth) { tokens += Indent }
            tokens += statementTokens.list
        }
        return Success(FormatTokens(tokens))
    }
}
