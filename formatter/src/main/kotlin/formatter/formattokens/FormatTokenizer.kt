package formatter.formattokens

import ast.AST
import ast.Block
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import formatter.FormattingError

interface FormatTokenizer {
    fun supports(ast: AST): Boolean

    fun tokenize(ast: AST): Either<FormattingError, FormatTokens>
}

// 1.0

class DeclarationFormatTokenizer : FormatTokenizer {
    override fun supports(ast: AST): Boolean = ast is AST.DeclarationStatement

    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (!supports(ast)) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        ast as AST.DeclarationStatement
        val first =
            if (ast.mutable) {
                Text("let")
            } else {
                Text("const")
            }
        val valueTokens =
            ast.value?.let { listOf(Text("=")) + expressionToFormatTokens(it) } ?: emptyList()
        return Success(
            FormatTokens(
                listOf(
                    first,
                    WhiteSpace,
                    Text(ast.id),
                    Text(":"),
                    Text(ast.type.name),
                ) + valueTokens + listOf(Text(";"), EOL),
            ),
        )
    }
}

class AssignmentFormatTokenizer : FormatTokenizer {
    override fun supports(ast: AST): Boolean = ast is AST.AssignmentStatement

    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (!supports(ast)) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        ast as AST.AssignmentStatement
        return Success(
            FormatTokens(
                listOf(
                    Text(ast.id),
                    Text("="),
                ) + expressionToFormatTokens(ast.value) + listOf(Text(";"), EOL),
            ),
        )
    }
}

class ExpressionFormatTokenizer : FormatTokenizer {
    override fun supports(ast: AST): Boolean = ast is AST.ExpressionStatement

    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (!supports(ast)) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        ast as AST.ExpressionStatement

        return Success(
            FormatTokens(
                expressionToFormatTokens(ast.expression) + listOf(Text(";"), EOL),
            ),
        )
    }
}

// 1.1

class ConditionalFormatTokenizer(
    private val statementTokenizers: () -> List<FormatTokenizer>,
) : FormatTokenizer {
    override fun supports(ast: AST): Boolean = ast is AST.ConditionalStatement

    override fun tokenize(ast: AST) = tokenizeAtDepth(ast, 1)

    private fun tokenizeAtDepth(
        ast: AST,
        depth: Int,
    ): Either<FormattingError, FormatTokens> {
        if (!supports(ast)) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        ast as AST.ConditionalStatement
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
                statementTokenizers().firstOrNull { it.supports(statement) }
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
