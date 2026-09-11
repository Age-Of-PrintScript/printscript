package versionfactory

import ast.ASTType
import interpreter.BinaryOperation
import interpreter.BuiltInFunction
import interpreter.OperationKey
import interpreter.statement.StatementEvaluator
import parser.builders.StatementParser
import tokens.TokenType
import versionfactory.v1_1.v1_1Keywords
import versionfactory.v1_1.v1_1Symbols

internal class PSVersion1_1 : PSVersion {
    override val binaryOperations: Map<OperationKey, BinaryOperation>
        get() = TODO("Not yet implemented")
    override val builtInFunctions: Map<String, BuiltInFunction>
        get() = TODO("Not yet implemented")
    override val keywords: Map<String, TokenType>
        get() = v1_1Keywords
    override val symbols: Map<Char, TokenType>
        get() = v1_1Symbols
    override val statementParsers: List<StatementParser>
        get() = TODO("Not yet implemented")
    override val statementEvaluators: Map<ASTType, StatementEvaluator>
        get() = TODO("Not yet implemented")
}
