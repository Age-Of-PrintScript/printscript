package formatter.formattokens

import ast.AST
import ast.Block
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import formatter.FormattingError

interface FormatTokenizer {
    fun tokenize(ast: AST): Either<FormattingError, FormatTokens>
}

// 1.0

class DeclarationFormatTokenizer : FormatTokenizer {
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

class AssignmentFormatTokenizer : FormatTokenizer {
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

class ExpressionFormatTokenizer : FormatTokenizer {
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

class ConditionalFormatTokenizer(
    private val statementFormatters: Set<FormatTokenizer>,
) : FormatTokenizer {
    override fun tokenize(ast: AST): Either<FormattingError, FormatTokens> {
        if (ast !is AST.ConditionalStatement) {
            return Failure(FormattingError.UNKNOWN_AST_TYPE)
        }
        val conditionTokens = expressionToFormatTokens(ast.condition)
        val thenTokens = tokenizeBlock(ast.ifBlock).getOrReturn { return Failure(it) }
        val elseTokens =
            ast.elseBlock?.let { elseBlock ->
                tokenizeBlock(elseBlock).getOrReturn { error -> return Failure(error) }
            }

        val header =
            listOf(Text("if"), WhiteSpace, Text("(")) + conditionTokens + listOf(Text(")"), WhiteSpace, Text("{"), EOL)
        val tail =
            elseTokens?.let {
                listOf(Text("}"), WhiteSpace, Text("else"), WhiteSpace, Text("{"), EOL) + it.list + listOf(Text("}"), EOL)
            } ?: listOf(Text("}"), EOL)

        return Success(FormatTokens(header + thenTokens.list + tail))
    }

    // Cada sentencia del bloque puede ser cualquier AST (declaración, asignación, expresión,
    // incluso otro Conditional anidado) y no sabemos cuál de antemano: probamos cada tokenizer
    // conocido hasta encontrar el que puede parsearla, igual que hace Executor.formatterFor
    // a nivel de programa completo, y le aplicamos sus propias reglas.

    private fun tokenizeBlock(block: Block): Either<FormattingError, FormatTokens> {
        val tokens = mutableListOf<FormatToken>()
        for (statement in block.statements) {
            val tokenizer =
                statementFormatters.firstOrNull { it.tokenize(statement) is Success }
                    ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)

            val statementTokens = tokenizer.tokenize(statement).getOrReturn { return Failure(it) }

            tokens += Indent
            tokens += statementTokens.list
        }
        return Success(FormatTokens(tokens))
    }
}
