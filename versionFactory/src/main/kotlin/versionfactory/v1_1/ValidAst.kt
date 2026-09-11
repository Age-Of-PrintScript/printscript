package versionfactory.v1_1

import ast.ASTType
import interpreter.statement.StatementEvaluator
import parser.BlockParser
import parser.ExpressionParser
import versionfactory.v1_0.v1_0BlockParser
import versionfactory.v1_0.v1_0StatementEvaluators

internal val expressionParser = ExpressionParser()

internal val v1_1BlockParser: BlockParser =
    v1_0BlockParser

internal val v1_1StatementEvaluators: Map<ASTType, StatementEvaluator> =
    v1_0StatementEvaluators
