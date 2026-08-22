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
    fun format(program: Program, rules: FormattingRules): Either<ParsingError, String>
}

class FormatterImpl: Formatter {

    override fun format(program: Program, rules: FormattingRules): Either<ParsingError, String> {
        var finalString = ""
        val asts = program.trees
        for (ast in asts) {
            val fileLine = formatLine(ast).getOrReturn { return Failure(it) }
            finalString += fileLine
        }
        return Success(finalString)

    }

    fun formatLine(ast: AST): Either<ParsingError, String> {
        when (ast) {
            is AST.Assignment -> {
                    val result = handleAssignmentParser().getOrReturn { return Failure(it) }
                }
                is AST.Call -> {
                    val result = handleCallParser().getOrReturn { return Failure(it) }
                }
                is AST.Declaration -> {
                    val result = handleDeclarationParser().getOrReturn { return Failure(it) }
                }
                }
        return Failure(ParsingError.PARSE_ERROR)
            }





    fun handleCallParser(): Either<ParsingError, AST.Assignment> {
        TODO()
    }

    fun handleAssignmentParser(): Either<ParsingError, AST.Assignment> {
        TODO()
    }

    fun handleDeclarationParser(): Either<ParsingError, AST.Assignment> {
        TODO()
    }

}