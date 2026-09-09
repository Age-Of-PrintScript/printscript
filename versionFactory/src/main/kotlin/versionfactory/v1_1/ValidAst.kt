package versionfactory.v1_1

import ast.ASTType
import interpreter.statement.StatementEvaluator
import parser.ExpressionParser
import parser.builders.StatementParser
import versionfactory.v1_0.v1_0StatementEvaluators
import versionfactory.v1_0.v1_0StatementParsers

internal val expressionParser = ExpressionParser()

internal val v1_1StatementParsers: List<StatementParser> =
    v1_0StatementParsers

internal val v1_1StatementEvaluators: Map<ASTType, StatementEvaluator> =
    v1_0StatementEvaluators
