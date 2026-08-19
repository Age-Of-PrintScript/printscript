package interpreter

import domain.PrintScriptType
import domain.PrintScriptValue
import junit.framework.TestCase.assertTrue
import java.util.Optional
import kotlin.test.assertEquals

internal fun assertEnvWithVariable(
    id: String,
    type: PrintScriptType,
    value: Optional<PrintScriptValue>,
    env: RuntimeEnvironment
){
    assertTrue(env.variableMap.containsKey(id))
    val info = env.variableMap[id]!!
    val envType = info.type
    val envValue = info.value
    assertEquals(type,envType)
    assertEquals(value, envValue)
}