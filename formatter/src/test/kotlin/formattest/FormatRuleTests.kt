package formattest

import ast.AST
import domain.getOrReturn
import formatter.formatrules.LinesAfterCall
import formatter.formatrules.SpaceAfterColon
import formatter.formatrules.SpaceAroundAssign
import formatter.formatrules.SpaceBeforeColon
import formatter.formattokens.AssignmentFormatTokenizer
import formatter.formattokens.DeclarationFormatTokenizer
import formatter.formattokens.EOL
import formatter.formattokens.ExpressionFormatTokenizer
import formatter.formattokens.FormatTokenizer
import formatter.formattokens.FormatTokens
import formatter.formattokens.Text
import formatter.formattokens.WhiteSpace
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testframework.createAssignment
import testframework.createDeclaration
import testframework.createPrintln

private fun tokensFrom(
    tokenizer: FormatTokenizer,
    ast: AST,
): FormatTokens = tokenizer.tokenize(ast).getOrReturn { error("tokenizer fallo: $it") }

class FormatRuleTests {
    // ------------------ SpaceAroundAssign ------------------------------

    @Test
    fun `space around assign - lista real de AssignmentFormatTokenizer agrega espacios`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))
        val equalsIndex = input.list.indexOfFirst { it is Text && it.value == "=" }
        val expected =
            input.list.toMutableList().apply {
                add(equalsIndex + 1, WhiteSpace)
                add(equalsIndex, WhiteSpace)
            }

        val result = SpaceAroundAssign(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space around assign - ya tiene un espacio a cada lado no cambia`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))

        val result = SpaceAroundAssign(true).apply(input)

        assertEquals(input, result)
    }

    @Test
    fun `space around assign - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))
        val rule = SpaceAroundAssign(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space around assign - raro ya tenia espacio de un solo lado completa el que falta`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), Text("5")))
        val expected = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))

        val result = SpaceAroundAssign(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space around assign - desactivada no toca nada`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))

        val result = SpaceAroundAssign(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ SpaceBeforeColon ------------------------------

    @Test
    fun `space before colon - lista real de DeclarationFormatTokenizer agrega espacio antes`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val colonIndex = input.list.indexOfFirst { it is Text && it.value == ":" }
        val expected = input.list.toMutableList().apply { add(colonIndex, WhiteSpace) }

        val result = SpaceBeforeColon(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space before colon - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val rule = SpaceBeforeColon(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space before colon - raro el dos puntos es el primer token`() {
        val input = FormatTokens(listOf(Text(":"), Text("string")))
        val expected = FormatTokens(listOf(WhiteSpace, Text(":"), Text("string")))

        val result = SpaceBeforeColon(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space before colon - desactivada no toca nada`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))

        val result = SpaceBeforeColon(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ SpaceAfterColon ------------------------------

    @Test
    fun `space after colon - lista real de DeclarationFormatTokenizer agrega espacio despues`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val colonIndex = input.list.indexOfFirst { it is Text && it.value == ":" }
        val expected = input.list.toMutableList().apply { add(colonIndex + 1, WhiteSpace) }

        val result = SpaceAfterColon(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space after colon - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val rule = SpaceAfterColon(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space after colon - raro el dos puntos es el ultimo token`() {
        val input = FormatTokens(listOf(Text("x"), Text(":")))
        val expected = FormatTokens(listOf(Text("x"), Text(":"), WhiteSpace))

        val result = SpaceAfterColon(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space after colon - desactivada no toca nada`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))

        val result = SpaceAfterColon(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ LinesAfterCall ------------------------------

    @Test
    fun `lines after call - lista real de ExpressionFormatTokenizer ajusta al valor configurado`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val withoutTrailingEol = input.list.dropLastWhile { it is EOL }
        val expected = FormatTokens(withoutTrailingEol + listOf(EOL, EOL))

        val result = LinesAfterCall(2).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val rule = LinesAfterCall(2)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `lines after call - raro cero lineas elimina el EOL que trae el tokenizer`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val expected = FormatTokens(input.list.dropLastWhile { it is EOL })

        val result = LinesAfterCall(0).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - raro lista sin ningun EOL igual agrega la cantidad configurada`() {
        val input = FormatTokens(listOf(Text("println(x)")))
        val expected = FormatTokens(listOf(Text("println(x)"), EOL, EOL, EOL))

        val result = LinesAfterCall(3).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - raro cantidad negativa no explota y no agrega lineas`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val expected = FormatTokens(input.list.dropLastWhile { it is EOL })

        val result = LinesAfterCall(-1).apply(input)

        assertEquals(expected, result)
    }
}
