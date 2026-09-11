package parser.testFramework

import ast.AST
import ast.Block
import ast.Expression
import domain.BoolType
import domain.NumType
import domain.PSType
import domain.StrType

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
            val id = getContent(lines, index + 1)
            val type = parsePSType(getContent(lines, index + 2))
            createDeclaration(index + 3, lines, childDepth, id, type)
        }
        "ASSIGNMENT" -> {
            val id = getContent(lines, index + 1)
            val (value, next) = parseExpression(lines, index + 2)
            AST.AssignmentStatement(id, value) to next
        }
        "CALL" -> {
            val functionName = getContent(lines, index + 1).lowercase()
            val (arg, next) = parseExpression(lines, index + 2)
            AST.ExpressionStatement(Expression.Call(functionName, listOf(arg))) to next
        }
        "CONDITIONAL" -> parseConditional(lines, index, childDepth)
        else -> throw IllegalArgumentException("AST desconocido: ${head.content}")
    }
}

private fun parseConditional(
    lines: List<Line>,
    index: Int,
    childDepth: Int,
): Pair<AST.ConditionalStatement, Int> {
    val (condition, afterCondition) = parseExpression(lines, index + 1)
    require(afterCondition < lines.size && lines[afterCondition].content == "THEN") {
        "Se esperaba THEN en condicional"
    }
    val (thenBlock, afterThen) = parseBlock(lines, afterCondition + 1, childDepth + 1)

    if (afterThen < lines.size && lines[afterThen].content == "ELSE") {
        val (elseBlock, afterElse) = parseBlock(lines, afterThen + 1, childDepth + 1)
        return AST.ConditionalStatement(condition, thenBlock, elseBlock) to afterElse
    }

    return AST.ConditionalStatement(condition, thenBlock, null) to afterThen
}

private fun parseBlock(
    lines: List<Line>,
    startIndex: Int,
    blockDepth: Int,
): Pair<Block, Int> {
    val statements = mutableListOf<AST>()
    var i = startIndex
    while (i < lines.size && lines[i].depth >= blockDepth) {
        if (lines[i].depth == blockDepth) {
            val (stmt, next) = parseAST(lines, i)
            statements.add(stmt)
            i = next
        } else {
            i++
        }
    }
    return Block(statements) to i
}

private fun parsePSType(name: String): PSType =
    when (name.uppercase()) {
        "NUMBER" -> NumType
        "STRING" -> StrType
        "BOOLEAN" -> BoolType
        else -> throw IllegalArgumentException("Tipo desconocido: $name")
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
    id: String,
    type: PSType,
): Pair<AST.DeclarationStatement, Int> {
    if (index < lines.size && lines[index].depth == childDepth) {
        val (value, next) = parseExpression(lines, index)
        return AST.DeclarationStatement(id, type, mutable = true, value = value) to next
    } else {
        return AST.DeclarationStatement(id, type, mutable = true, value = null) to index
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
    val psType = parsePSType(literalType)
    return Expression.Literal(value, psType) to index + 1
}

private fun getOperation(
    line: Line,
    lines: List<Line>,
    index: Int,
): Pair<Expression.Operation, Int> {
    val op = TestOperator.valueOf(getValue(line))
    val (left, afterLeft) = parseExpression(lines, index + 1)
    val (right, afterRight) = parseExpression(lines, afterLeft)
    return Expression.Operation(left, op, right) to afterRight
}

private fun getValue(line: Line): String = line.content.substringAfter(" ").trim()
