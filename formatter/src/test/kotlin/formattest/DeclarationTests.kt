package formattest

import domain.NumType
import domain.StrType
import formatter.FormatterImplementation
import formatter.FormattingRules
import formatter.SemiColonAtTheEndRule
import formatter.SpaceAfterColonRule
import formatter.SpaceBeforeColonRule
import formatter.SpacesAroundAssignRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testframework.createDeclaration
import testframework.createNumberLiteralExpression
import testframework.createStringLiteralExpression

// este framework testea las reglas, no el excecutor

class DeclarationTests {
    // ------------------ Grupo A: combinaciones de reglas activas/inactivas ------------------

    @Test
    fun `todas las reglas activas - caso base`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(true),
                SpaceAfterColonRule(true),
                SpacesAroundAssignRule(true),
                SemiColonAtTheEndRule(true),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x : string = \"hello\";", result)
    }

    @Test
    fun `todas las reglas inactivas`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(false),
                SpaceAfterColonRule(false),
                SpacesAroundAssignRule(false),
                SemiColonAtTheEndRule(false),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x:string=\"hello\"", result)
    }

    @Test
    fun `solo espacio antes de los dos puntos activo`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(true),
                SpaceAfterColonRule(false),
                SpacesAroundAssignRule(false),
                SemiColonAtTheEndRule(false),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x :string=\"hello\"", result)
    }

    @Test
    fun `solo espacio despues de los dos puntos activo`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(false),
                SpaceAfterColonRule(true),
                SpacesAroundAssignRule(false),
                SemiColonAtTheEndRule(false),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x: string=\"hello\"", result)
    }

    @Test
    fun `solo espacios alrededor del igual activo`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(false),
                SpaceAfterColonRule(false),
                SpacesAroundAssignRule(true),
                SemiColonAtTheEndRule(false),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x:string = \"hello\"", result)
    }

    @Test
    fun `solo punto y coma activo`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("hello"))
        val rules =
            listOf(
                SpaceBeforeColonRule(false),
                SpaceAfterColonRule(false),
                SpacesAroundAssignRule(false),
                SemiColonAtTheEndRule(true),
            )
        val result = FormatterImplementation(FormattingRules(rules)).format(declaration)

        assertEquals("let x:string=\"hello\";", result)
    }

    // ------------------ Grupo B: contenido del string (todas las reglas activas) ------------------

    private val allRules =
        listOf(
            SpaceBeforeColonRule(true),
            SpaceAfterColonRule(true),
            SpacesAroundAssignRule(true),
            SemiColonAtTheEndRule(true),
        )

    @Test
    fun `string vacio`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression(""))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"\";", result)
    }

    @Test
    fun `string con comillas dentro`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("Dijo \"hola\""))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"Dijo \"hola\"\";", result)
    }

    @Test
    fun `string con dos puntos dentro`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("10:30"))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"10:30\";", result)
    }

    @Test
    fun `string con signo igual dentro`() {
        val declaration = createDeclaration("x", StrType, createStringLiteralExpression("a=b"))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"a=b\";", result)
    }

    @Test
    fun `string con espacios multiples dentro`() {
        val declaration =
            createDeclaration("x", StrType, createStringLiteralExpression("  con   espacios  "))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"  con   espacios  \";", result)
    }

    @Test
    fun `string con salto de linea dentro`() {
        val declaration =
            createDeclaration("x", StrType, createStringLiteralExpression("linea1\nlinea2"))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : string = \"linea1\nlinea2\";", result)
    }

    // ------------------ Grupo C: variaciones de identificador (todas las reglas activas) ------------------

    @Test
    fun `identificador de una sola letra`() {
        val declaration = createDeclaration("a", NumType, createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let a : number = 1;", result)
    }

    @Test
    fun `identificador largo`() {
        val declaration =
            createDeclaration("unNombreDeVariableMuyLargoParaProbar", NumType, createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let unNombreDeVariableMuyLargoParaProbar : number = 1;", result)
    }

    @Test
    fun `identificador en snake_case`() {
        val declaration =
            createDeclaration("mi_variable_con_guiones_bajos", NumType, createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let mi_variable_con_guiones_bajos : number = 1;", result)
    }

    @Test
    fun `identificador con numeros al final`() {
        val declaration = createDeclaration("variable2", NumType, createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let variable2 : number = 1;", result)
    }

    // ------------------ Grupo D: valores numericos (todas las reglas activas) ------------------

    @Test
    fun `numero cero`() {
        val declaration = createDeclaration("x", NumType, createNumberLiteralExpression(0))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : number = 0;", result)
    }

    @Test
    fun `numero negativo`() {
        val declaration = createDeclaration("x", NumType, createNumberLiteralExpression(-5))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : number = -5;", result)
    }

    @Test
    fun `numero grande`() {
        val declaration = createDeclaration("x", NumType, createNumberLiteralExpression(999999999))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : number = 999999999;", result)
    }

    @Test
    fun `numero decimal`() {
        val declaration = createDeclaration("x", NumType, createNumberLiteralExpression(3.14))
        val result = FormatterImplementation(FormattingRules(allRules)).format(declaration)

        assertEquals("let x : number = 3.14;", result)
    }
}
