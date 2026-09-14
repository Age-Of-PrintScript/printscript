package formattest

import ast.AST
import ast.Expression
import formatter.formatrules.EnsureNoSpaceAroundEquals
import formatter.formatrules.EnsureSpaceAfterColon
import formatter.formatrules.EnsureSpaceAroundEquals
import formatter.formatrules.EnsureSpaceBeforeColon
import formatter.formatrules.LineBreaksAfterPrintLn
import formatter.formattokens.AssignmentFormatTokenizer
import formatter.formattokens.DeclarationFormatTokenizer
import formatter.formattokens.EOL
import formatter.formattokens.ExpressionFormatTokenizer
import formatter.formattokens.FormatTokens
import formatter.formattokens.Text
import formatter.formattokens.WhiteSpace
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testframework.createAssignment
import testframework.createDeclaration
import testframework.createPrintln
import testframework.createStringLiteralExpression
import testframework.tokensFrom

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

        val result = EnsureSpaceAroundEquals(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space around assign - ya tiene un espacio a cada lado no cambia`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))

        val result = EnsureSpaceAroundEquals(true).apply(input)

        assertEquals(input, result)
    }

    @Test
    fun `space around assign - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))
        val rule = EnsureSpaceAroundEquals(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space around assign - raro ya tenia espacio de un solo lado completa el que falta`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), Text("5")))
        val expected = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))

        val result = EnsureSpaceAroundEquals(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space around assign - desactivada no toca nada`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))

        val result = EnsureSpaceAroundEquals(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ EnsureNoSpaceAroundEquals ------------------------------

    @Test
    fun `no space around equals - lista real de AssignmentFormatTokenizer ya no tiene espacios y no cambia`() {
        val input = tokensFrom(AssignmentFormatTokenizer(), createAssignment("x"))

        val result = EnsureNoSpaceAroundEquals(true).apply(input)

        assertEquals(input, result)
    }

    @Test
    fun `no space around equals - saca los espacios existentes a los dos lados`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))
        val expected = FormatTokens(listOf(Text("x"), Text("="), Text("5")))

        val result = EnsureNoSpaceAroundEquals(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `no space around equals - idempotencia aplicando dos veces seguidas`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))
        val rule = EnsureNoSpaceAroundEquals(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `no space around equals - desactivada no toca nada`() {
        val input = FormatTokens(listOf(Text("x"), WhiteSpace, Text("="), WhiteSpace, Text("5")))

        val result = EnsureNoSpaceAroundEquals(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ SpaceBeforeColon ------------------------------

    @Test
    fun `space before colon - lista real de DeclarationFormatTokenizer agrega espacio antes`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val colonIndex = input.list.indexOfFirst { it is Text && it.value == ":" }
        val expected = input.list.toMutableList().apply { add(colonIndex, WhiteSpace) }

        val result = EnsureSpaceBeforeColon(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space before colon - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val rule = EnsureSpaceBeforeColon(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space before colon - raro el dos puntos es el primer token`() {
        val input = FormatTokens(listOf(Text(":"), Text("string")))
        val expected = FormatTokens(listOf(WhiteSpace, Text(":"), Text("string")))

        val result = EnsureSpaceBeforeColon(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space before colon - desactivada no toca nada`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))

        val result = EnsureSpaceBeforeColon(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ SpaceAfterColon ------------------------------

    @Test
    fun `space after colon - lista real de DeclarationFormatTokenizer agrega espacio despues`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val colonIndex = input.list.indexOfFirst { it is Text && it.value == ":" }
        val expected = input.list.toMutableList().apply { add(colonIndex + 1, WhiteSpace) }

        val result = EnsureSpaceAfterColon(true).apply(input)

        assertEquals(FormatTokens(expected), result)
    }

    @Test
    fun `space after colon - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))
        val rule = EnsureSpaceAfterColon(true)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `space after colon - raro el dos puntos es el ultimo token`() {
        val input = FormatTokens(listOf(Text("x"), Text(":")))
        val expected = FormatTokens(listOf(Text("x"), Text(":"), WhiteSpace))

        val result = EnsureSpaceAfterColon(true).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `space after colon - desactivada no toca nada`() {
        val input = tokensFrom(DeclarationFormatTokenizer(), createDeclaration("x"))

        val result = EnsureSpaceAfterColon(false).apply(input)

        assertEquals(input, result)
    }

    // ------------------ LinesAfterCall ------------------------------

    @Test
    fun `lines after call - lista real de ExpressionFormatTokenizer ajusta al valor configurado`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val withoutTrailingEol = input.list.dropLastWhile { it is EOL }
        val expected = FormatTokens(withoutTrailingEol + listOf(EOL, EOL, EOL))

        val result = LineBreaksAfterPrintLn(2).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - idempotencia aplicando dos veces seguidas`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val rule = LineBreaksAfterPrintLn(2)

        val once = rule.apply(input)
        val twice = rule.apply(once)

        assertEquals(once, twice)
    }

    @Test
    fun `lines after call - raro cero lineas elimina el EOL que trae el tokenizer`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val expected = FormatTokens(input.list.dropLastWhile { it is EOL } + listOf(EOL))

        val result = LineBreaksAfterPrintLn(0).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - raro cantidad negativa no explota y no agrega lineas`() {
        val input = tokensFrom(ExpressionFormatTokenizer(), createPrintln())
        val expected = FormatTokens(input.list.dropLastWhile { it is EOL })

        val result = LineBreaksAfterPrintLn(-1).apply(input)

        assertEquals(expected, result)
    }

    @Test
    fun `lines after call - no afecta llamadas que no son println`() {
        val readInput = AST.ExpressionStatement(Expression.Call("readInput", listOf(createStringLiteralExpression("nombre"))))
        val input = tokensFrom(ExpressionFormatTokenizer(), readInput)

        val result = LineBreaksAfterPrintLn(2).apply(input)

        assertEquals(input, result)
    }
}
