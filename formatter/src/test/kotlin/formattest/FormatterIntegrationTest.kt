package formattest

import formatter.FormatError
import formatter.FormatResult
import formatter.FormatSuccess
import formatter.Formatter
import formattest.cases.FormatterFailureCases
import formattest.cases.FormatterSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.io.TempDir
import testframework.createDefaultConfig10
import testframework.createDefaultConfig11
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
    fun `failed formatting integration cases`(): List<DynamicNode> {
        val missingConfigPath = tempDir.resolve("missing_config.json").toString()
        return FormatterFailureCases.cases(missingConfigPath).map { case ->
            dynamicTest(case.name) {
                val result = executeFormatter(case.version, case.source, case.configJson, case.customConfigPath)
                assertInstanceOf(FormatError::class.java, result)
                case.expectedErrorMessage?.let { expectedMsg ->
                    assertEquals(expectedMsg, (result as FormatError).value)
                }
            }
        }
    }

    private fun executeFormatter(
        version: String,
        source: String,
        configJson: String?,
        customConfigPath: String? = null,
    ): FormatResult<String, String> {
        val scriptFile = tempDir.resolve("script.ps").toFile()
        scriptFile.writeText(source)

        val configPath =
            if (customConfigPath != null) {
                customConfigPath
            } else {
                val configFile = tempDir.resolve("rules.json").toFile()
                configFile.writeText(configJson ?: "{}")
                configFile.absolutePath
            }

        val configProvider = if (version == "1.1") createDefaultConfig11() else createDefaultConfig10()
        val formatter = Formatter.new(configProvider, version)
        return formatter.execute(scriptFile, configPath)
    }
}
