package versionfactory

import ast.ASTType
import interpreter.BinaryOperation
import interpreter.BuiltInFunction
import interpreter.OperationKey
import interpreter.statement.StatementEvaluator
import parser.builders.StatementParser
import tokens.TokenType
import versionfactory.v1_0.v1_0BinaryOperations
import versionfactory.v1_0.v1_0Keywords
import versionfactory.v1_0.v1_0StatementEvaluators
import versionfactory.v1_0.v1_0StatementParsers
import versionfactory.v1_0.v1_0Symbols
import versionfactory.v1_0.v1_0builtInFunctions

internal class PSVersion1_0 : PSVersion {
    override val binaryOperations: Map<OperationKey, BinaryOperation>
        get() = v1_0BinaryOperations
    override val builtInFunctions: Map<String, BuiltInFunction>
        get() = v1_0builtInFunctions
    override val keywords: Map<String, TokenType>
        get() = v1_0Keywords
    override val symbols: Map<Char, TokenType>
        get() = v1_0Symbols
    override val statementParsers: List<StatementParser>
        get() = v1_0StatementParsers
    override val statementEvaluators: Map<ASTType, StatementEvaluator>
        get() = v1_0StatementEvaluators
}
