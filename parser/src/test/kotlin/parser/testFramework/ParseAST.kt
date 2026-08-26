package parser.testFramework

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

private data class Line(
    val depth: Int,
    val content: String,
)

internal fun parseExpectedTrees(rawLines: List<String>): List<AST> {
    val lines = rawLines.map { toLine(it) }
    val trees = mutableListOf<AST>()

    var i = 0
    while (i < lines.size) {
        val (tree, next) = parseAST(lines, i)
        trees.add(tree)
        i = next
    }
    return trees
}

private fun toLine(raw: String): Line {
    val depth = raw.takeWhile { it == ' ' || it == '\t' }.length / 4
    return Line(depth, raw.trim())
}

private fun parseAST(
    lines: List<Line>,
    index: Int,
): Pair<AST, Int> {
    val head = lines[index]
    val childDepth = head.depth + 1

    return when (head.content) {
        "DECLARATION" -> {
            val id = ASTIdentifier(getContent(lines, index + 1))
            val type = ASTDataType(PrintScriptType.valueOf(getContent(lines, index + 2)))
            createDeclaration(index + 3, lines, childDepth, id, type)
        }
        "ASSIGNMENT" -> {
            val id = ASTIdentifier(getContent(lines, index + 1))
            val (value, next) = parseExpression(lines, index + 2)
            AST.Assignment(id, value) to next
        }
        "CALL" -> {
            val functionName = PrintScriptFunctions.valueOf(getContent(lines, index + 1))
            val (arg, next) = parseExpression(lines, index + 2)
            AST.Call(functionName, listOf(arg)) to next
        }
        else -> throw IllegalArgumentException("AST desconocido: ${head.content}")
    }
}

private fun parseExpression(
    lines: List<Line>,
    index: Int,
): Pair<Expression, Int> {
    val line = lines[index]

    return when {
        line.content.startsWith("OPERATION") -> getOperation(line, lines, index)

        line.content.startsWith("LITERAL") -> getLiteral(line, index)

        line.content.startsWith("VARIABLE") ->
            Expression.Variable(getValue(line)) to index + 1

        else -> throw IllegalArgumentException("Expression desconocida: ${line.content}")
    }
}

private fun createDeclaration(
    index: Int,
    lines: List<Line>,
    childDepth: Int,
    id: ASTIdentifier,
    type: ASTDataType,
): Pair<AST.Declaration, Int> {
    if (index < lines.size && lines[index].depth == childDepth) {
        val (value, next) = parseExpression(lines, index)
        return AST.Declaration(id, type, value) to next
    } else {
        return AST.Declaration(id, type, null) to index
    }
}

private fun getContent(
    lines: List<Line>,
    index: Int,
): String = lines[index].content

private fun getLiteral(
    line: Line,
    index: Int,
): Pair<Expression.Literal, Int> {
    val rest = getValue(line)
    val literalType = rest.substringBefore(" ")
    val value = rest.substringAfter(" ")
    val literal =
        when (literalType) {
            "NUMBER" -> PrintScriptValue.NumberLiteral(value.toDouble())
            "STRING" -> PrintScriptValue.StringLiteral(value)
            else -> throw IllegalArgumentException("Tipo de literal desconocido: $literalType")
        }
    return Expression.Literal(literal) to index + 1
}

private fun getOperation(
    line: Line,
    lines: List<Line>,
    index: Int,
): Pair<Expression.Operation, Int> {
    val op = PrintScriptOperator.valueOf(getValue(line))
    val (left, afterLeft) = parseExpression(lines, index + 1)
    val (right, afterRight) = parseExpression(lines, afterLeft)
    return Expression.Operation(left, right, op) to afterRight
}

private fun getValue(line: Line): String = line.content.substringAfter(" ").trim()
