package formatter

import ast.AST
import ast.Expression
import ast.ExpressionSolver
import domain.Either
import domain.getOrReturn
import ast.Program
import domain.Failure
import domain.PrintScriptFunctions
import domain.PrintScriptType
import domain.PrintScriptValue
import domain.Success
import java.util.Optional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class FormattingRules(
    @SerialName("space before colon") val spaceBeforeColon: Boolean,
    @SerialName("space after colon") val spaceAfterColon: Boolean,
    @SerialName("spaces around assign") val spacesAroundAssign: Boolean,
    @SerialName("lines before call") val linesBeforeCall: Int
)

class RulesLoader { // Para el executor
    fun loadRules(jsonText: String): FormattingRules {
        return Json.decodeFromString<FormattingRules>(jsonText)
    }

    fun loadRulesFromFile(path: String): FormattingRules {
        val text = java.io.File(path).readText()
        return Json.decodeFromString(text)
    }
}

interface Formatter {
    fun format(program: Program, rules: FormattingRules): String
}

class FormatterImpl: Formatter {

    override fun format(program: Program, rules: FormattingRules): String {
        var finalString = ""
        val asts = program.trees

        for (ast in asts) {
            val fileLine = formatLine(ast, rules)
            finalString += fileLine
        }
        return finalString

    }

    fun formatLine(ast: AST, rules: FormattingRules): String {
        return when (ast) {
            is AST.Assignment -> {
                    val result = handleAssignmentParser(ast, rules)
                   result
                }
                is AST.Call -> {
                    val result = handleCallParser(ast, rules)
                    result
                }
                is AST.Declaration -> {
                    val result = handleDeclarationParser(ast, rules)
                    result
                }
            }
        }

    fun handleCallParser(ast: AST.Call, rules: FormattingRules): String {
        val line = "${ast.functionName}(${ast.args})"
        val linesBeforeCall = rules.linesBeforeCall
        val functionName = ast.functionName

        return when (functionName) {
            PrintScriptFunctions.PRINTLN -> {
                val newlines = "\n".repeat(linesBeforeCall) // "".repeat(n: Int) te devuelve el string "" repetido n veces
                newlines + line
            }
        }
    }

    fun handleAssignmentParser(ast: AST.Assignment, rules: FormattingRules): String {
        val separator = if (rules.spacesAroundAssign) " = " else "="
        val expression = ast.value.toString() //no se como sale esto, creo que puede devolver el AST[value = ...], deberiamos implementar un expressionToString
        return "${ast.id}$separator$expression"
    }

    fun handleDeclarationParser(ast: AST.Declaration, rules: FormattingRules): String {
        val id = ast.id
        val type = ast.type

        var final = "let $id"

        final += if (rules.spaceBeforeColon) " :" else ":"

        if (rules.spaceAfterColon) {
            final += " "
        }

        final += "$type"

        val separator = if (rules.spacesAroundAssign) " = " else "="
        val expression = expressionToString(ast.value)

        return final + separator + expression
    }

}