package formattest

import ast.AST
import domain.getOrReturn
import formatter.formatrules.IfBraceBelowLine
import formatter.formatrules.IfBraceSameLine
import formatter.formatrules.IndentsInsideIf
import formatter.formattokens.AssignmentFormatTokenizer
import formatter.formattokens.ConditionalFormatTokenizer
import formatter.formattokens.DeclarationFormatTokenizer
import formatter.formattokens.EOL
import formatter.formattokens.ExpressionFormatTokenizer
import formatter.formattokens.FormatTokenizer
import formatter.formattokens.FormatTokens
import formatter.formattokens.Indent
import formatter.formattokens.Text
import formatter.formattokens.WhiteSpace
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testframework.createConditional

private fun tokensFrom(
    tokenizer: FormatTokenizer,
    ast: AST,
): FormatTokens = tokenizer.tokenize(ast).getOrReturn { error("tokenizer fallo: $it") }

private fun conditionalTokenizer(): ConditionalFormatTokenizer =
    ConditionalFormatTokenizer(
        setOf(DeclarationFormatTokenizer(), AssignmentFormatTokenizer(), ExpressionFormatTokenizer()),
    )

class FormatRuleTests1_1 {
    // ------------------ IfBraceSameLine ------------------------------

    @Test
    fun `if brace same line - lista real ya tiene la llave en la misma linea y no cambia`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())

        val result = IfBraceSameLine(true).apply(input)

        assertEquals(input, result)
    }

    @Test
    fun `if brace same line - sube la llave de la linea siguiente a la misma linea`() {
        val input =
            FormatTokens(listOf(Text("if"), WhiteSpace, Text("("), Text("x"), Text(")"), EOL, Text("{"), EOL))
        val expected =
            FormatTokens(listOf(Text("if"), WhiteSpace, Text("("), Text("x"), Text(")"), WhiteSpace, Text("{"), EOL))

        val result = IfBraceSameLine(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `if brace same line - idempotencia aplicando dos veces seguidas`() {
        val input =
            FormatTokens(listOf(Text("if"), WhiteSpace, Text("("), Text("x"), Text(")"), EOL, Text("{"), EOL))
        val rule = IfBraceSameLine(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `if brace same line - desactivada no toca nada`() {
        val input =
            FormatTokens(listOf(Text("if"), WhiteSpace, Text("("), Text("x"), Text(")"), EOL, Text("{"), EOL))

        val result = IfBraceSameLine(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ IfBraceBelowLine ------------------------------

    @Test
    fun `if brace below line - lista real de ConditionalFormatTokenizer baja la llave a la linea siguiente`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())
        val braceIndex = input.list.indexOfFirst { it is Text && it.value == "{" }
        val expected =
            input.list.toMutableList().apply {
                removeAt(braceIndex - 1) // el WhiteSpace que separaba ")" de "{"
                add(braceIndex - 1, EOL)
            }

        val result = IfBraceBelowLine(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `if brace below line - ya esta en linea aparte y no cambia`() {
        val input =
            FormatTokens(listOf(Text("if"), WhiteSpace, Text("("), Text("x"), Text(")"), EOL, Text("{"), EOL))

        val result = IfBraceBelowLine(true).apply(input)

        assertEquals(input, result)
    }

    @Test
    fun `if brace below line - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())
        val rule = IfBraceBelowLine(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `if brace below line - desactivada no toca nada`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())

        val result = IfBraceBelowLine(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ IndentsInsideIf ------------------------------

    @Test
    fun `indents inside if - lista real de ConditionalFormatTokenizer expande el Indent configurado`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())
        val expected = FormatTokens(input.list.flatMap { if (it is Indent) List(4) { WhiteSpace } else listOf(it) })

        val result = IndentsInsideIf(4).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `indents inside if - varios Indent seguidos (anidamiento) se expanden todos por igual`() {
        val input =
            FormatTokens(listOf(Indent, Indent, Text("println"), Text("("), Text("x"), Text(")"), Text(";"), EOL))
        val expected =
            FormatTokens(List(4) { WhiteSpace } + listOf(Text("println"), Text("("), Text("x"), Text(")"), Text(";"), EOL))

        val result = IndentsInsideIf(2).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `indents inside if - con 0 elimina el Indent sin agregar espacios`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())
        val expected = FormatTokens(input.list.filter { it !is Indent })

        val result = IndentsInsideIf(0).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `indents inside if - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(conditionalTokenizer(), createConditional())
        val rule = IndentsInsideIf(4)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }
}
