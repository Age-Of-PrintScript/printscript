package ast

import domain.PrintScriptValue

/*
    * Manejo de operaciones básicas
    * */

//String

fun handleStringSum(left: String, right: String): PrintScriptValue{
    return PrintScriptValue.StringLiteral(left + right)
}

//Number

fun handleNumberSum(left: Number, right: Number): PrintScriptValue{
    return PrintScriptValue.NumberLiteral(left.toDouble() + right.toDouble())
}

fun handleNumberSubtract(left: Number, right: Number): PrintScriptValue{
    return PrintScriptValue.NumberLiteral(left.toDouble() - right.toDouble())
}

fun handleNumberProduct(left: Number, right: Number): PrintScriptValue{
    return PrintScriptValue.NumberLiteral(left.toDouble() * right.toDouble())
}

fun handleNumberDivide(left: Number, right: Number): PrintScriptValue{
    return PrintScriptValue.NumberLiteral(left.toDouble() / right.toDouble())
}