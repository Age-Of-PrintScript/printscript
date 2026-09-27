package formattest

import domain.getOrReturn
import formatter.ConfigProvider
import formatter.FormatError
import formatter.FormatResult
import formatter.FormatSuccess
import formatter.Formatter
import formatter.applyJsonConfig
import formatter.formatrules.EnsureSpaceAroundEquals
import formatter.formatrules.FormatRules
import formatter.formattokens.AssignmentFormatTokenizer
import formattest.cases.FormatterFailureCases
import formattest.cases.FormatterSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class FormatterIntegrationTest {
    @TempDir
    lateinit var tempDir: Path

    @TestFactory
    fun `successful formatting integration cases`(): List<DynamicNode> =
        FormatterSuccessCases.cases().map { case ->
            dynamicTest(case.name) {
                val result = executeFormatter(case.version, case.source, case.configJson)
                assertInstanceOf(FormatSuccess::class.java, result)
                val output = (result as FormatSuccess).value
                assertEquals(
                    case.expectedOutput.replace("\n", System.lineSeparator()),
                    output,
                )
            }
        }

    @TestFactory
    fun `failed formatting integration cases`(): List<DynamicNode> =
        FormatterFailureCases.cases().map { case ->
            dynamicTest(case.name) {
                val result = executeFormatter(case.version, case.source, case.configJson)
                assertInstanceOf(FormatError::class.java, result)
                case.expectedErrorMessage?.let { expectedMsg ->
                    assertEquals(expectedMsg, (result as FormatError).value)
                }
            }
        }

    @Test
    fun `one formatter instance supports independent versioned requests`() {
        val formatter = Formatter.create()

        val version10 =
            formatter.format(
                source = "let x:number=1;",
                version = "1.0",
                configJson = """{"enforce-spacing-around-equals": true, "enforce-spacing-after-colon-in-declaration": true}""",
            )
        val version11 =
            formatter.format(
                source = "if (true) { println(\"hello\"); }",
                version = "1.1",
                configJson = """{"if-brace-same-line": true, "indent-inside-if": 2}""",
            )

        assertEquals("let x: number = 1;${System.lineSeparator()}", (version10 as FormatSuccess).value)
        assertEquals(
            "if (true) {${System.lineSeparator()}  println(\"hello\");${System.lineSeparator()}}${System.lineSeparator()}",
            (version11 as FormatSuccess).value,
        )
    }

    @Suppress("DEPRECATION")
    @Test
    fun `legacy file api remains compatible`() {
        val scriptFile = tempDir.resolve("legacy-script.ps").toFile()
        scriptFile.writeText("let x:number=1;")
        val configFile = tempDir.resolve("legacy-rules.json").toFile()
        configFile.writeText("""{"enforce-spacing-around-equals": true, "enforce-spacing-after-colon-in-declaration": true}""")

        val formatter = Formatter.new(ConfigProvider.defaultFor("1.0"), "1.0")
        val result = formatter.execute(scriptFile, configFile.absolutePath)

        assertEquals("let x: number = 1;${System.lineSeparator()}", (result as FormatSuccess).value)
    }

    @Test
    fun `formatter validates known request configuration`() {
        val formatter = Formatter.create()

        val unknownOption = formatter.format("let x:number=1;", "1.0", """{"unknown-option": true}""")
        val conflictingOptions =
            formatter.format(
                "let x:number=1;",
                "1.0",
                """{"enforce-spacing-around-equals": true, "enforce-no-spacing-around-equals": true}""",
            )
        val invalidValue = formatter.format("let x:number=1;", "1.1", """{"indent-inside-if": -1}""")

        assertInstanceOf(FormatSuccess::class.java, unknownOption)
        assertEquals(
            "Options 'enforce-spacing-around-equals' and 'enforce-no-spacing-around-equals' cannot both be enabled",
            (conflictingOptions as FormatError).value,
        )
        assertEquals("Option 'indent-inside-if' must be greater than or equal to 0", (invalidValue as FormatError).value)
    }

    @Suppress("DEPRECATION")
    @Test
    fun `explicit false overrides a default rule`() {
        val default =
            ConfigProvider(
                mapOf(
                    AssignmentFormatTokenizer() to FormatRules(listOf(EnsureSpaceAroundEquals(true))),
                ),
            )

        val resolved =
            applyJsonConfig(default, """{"enforce-spacing-around-equals": false}""")
                .getOrReturn { error("configuration should be valid: ${it.getMessage()}") }
        val rule =
            resolved.ruleSet
                .values
                .single()
                .list
                .single() as EnsureSpaceAroundEquals

        assertFalse(rule.activated)
    }

    private fun executeFormatter(
        version: String,
        source: String,
        configJson: String?,
    ): FormatResult<String, String> = Formatter.create().format(source, version, configJson)
}
