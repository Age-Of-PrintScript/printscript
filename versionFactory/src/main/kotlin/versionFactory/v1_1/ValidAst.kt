package versionFactory.v1_1

import ast.ASTType
import interpreter.statement.StatementEvaluator
import parser.ExpressionParser
import parser.builders.StatementParser
import versionFactory.v1_0.v1_0StatementEvaluators
import versionFactory.v1_0.v1_0StatementParsers

val expressionParser = ExpressionParser()

val v1_1StatementParsers: Map<ASTType, StatementParser> =
    v1_0StatementParsers

val v1_1StatementEvaluators: Map<ASTType, StatementEvaluator> =
    v1_0StatementEvaluators
