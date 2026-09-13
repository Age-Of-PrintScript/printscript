package versionfactory

import ast.ASTType
import interpreter.BinaryOperation
import interpreter.BuiltInFunction
import interpreter.CastKey
import interpreter.OperationKey
import interpreter.TypeCast
import interpreter.statement.StatementEvaluator
import parser.builders.StatementParser
import tokens.TokenType
import versionfactory.v1_0.v1_0BinaryOperations
import versionfactory.v1_1.v1_1Keywords
import versionfactory.v1_1.v1_1StatementEvaluators
import versionfactory.v1_1.v1_1StatementParsers
import versionfactory.v1_1.v1_1Symbols
import versionfactory.v1_1.v1_1TypeCasters
import versionfactory.v1_1.v1_1builtInFunctions

internal class PSVersion1_1 : PSVersion {
    override val binaryOperations: Map<OperationKey, BinaryOperation>
        get() = v1_0BinaryOperations
    override val builtInFunctions: Map<String, BuiltInFunction>
        get() = v1_1builtInFunctions
    override val keywords: Map<String, TokenType>
        get() = v1_1Keywords
    override val symbols: Map<Char, TokenType>
        get() = v1_1Symbols
    override val statementParsers: List<StatementParser>
        get() = v1_1StatementParsers
    override val statementEvaluators: Map<ASTType, StatementEvaluator>
        get() = v1_1StatementEvaluators
    override val typeCasters: Map<CastKey, TypeCast>
        get() = v1_1TypeCasters
}
