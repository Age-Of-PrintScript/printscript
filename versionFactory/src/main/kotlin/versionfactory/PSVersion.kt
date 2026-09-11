package versionfactory

import ast.ASTType
import domain.Either
import domain.Failure
import domain.Success
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
    val statementParsers: List<StatementParser>
    val statementEvaluators: Map<ASTType, StatementEvaluator>

    companion object {
        fun getVersion(version: String): Either<VersionError, PSVersion> =
            when (version) {
                "1.0" -> Success(version1_0)
                else -> Failure(VersionError.VERSION_DOESNT_EXISTS)
            }

        fun getLatestVersion(): PSVersion = version1_0
    }
}

val version1_0: PSVersion = PSVersion1_0()
