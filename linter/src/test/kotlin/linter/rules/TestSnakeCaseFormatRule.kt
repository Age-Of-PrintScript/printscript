package linter.rules

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.PrintScriptType
import domain.PrintScriptValue
import linter.IdentifierConvention
import kotlin.test.Test
import kotlin.test.assertTrue


class TestSnakeCaseFormatRule{
    @Test
    fun x() {
        testNoWarning("x")
    }
    @Test
    fun snake_case(){
        testNoWarning("snake_case")
    }
    @Test
    fun camelCase(){
        testWarning("camelCase")
    }
    @Test
    fun MAYUS(){
        testWarning("MAYUS")
    }



    private fun testWarning(identifier: String) {
        val format = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = getASTDECLARATION(identifier)
        val result = format.apply(ast)
        assertTrue(result.isPresent)
    }

    private fun testNoWarning(identifier: String){
        val format = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = getASTDECLARATION(identifier)
        val result = format.apply(ast)
        assertTrue(result.isEmpty)
    }


    private fun getASTDECLARATION(id: String): AST{
        return AST.Declaration(
            id = ASTIdentifier(id),
            type = ASTDataType(PrintScriptType.NUMBER),
            value = Expression.Literal(PrintScriptValue.NumberLiteral(5))
        )
    }
}

