package versionfactory

import ast.ASTType
import interpreter.BinaryOperation
import interpreter.BuiltInFunction
import interpreter.OperationKey
import interpreter.statement.StatementEvaluator
import parser.builders.StatementParser
import tokens.TokenType

interface PSVersion {
    val binaryOperations: Map<OperationKey, BinaryOperation>
    val builtInFunctions: Map<String, BuiltInFunction>
    val keywords: Map<String, TokenType>
    val symbols: Map<Char, TokenType>
    val statementParsers: Map<ASTType, StatementParser>
    val statementEvaluators: Map<ASTType, StatementEvaluator>
}

val version1_0: PSVersion = PSVersion1_0()
