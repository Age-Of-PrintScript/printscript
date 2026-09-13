package versionfactory.v1_1

import ast.ASTType
import interpreter.statement.StatementEvaluator
import parser.BlockParser
import parser.ExpressionParser
import parser.builders.ConditionalParser
import parser.builders.StatementParser
import versionfactory.v1_0.v1_0StatementEvaluators
import versionfactory.v1_0.v1_0StatementParsers

internal val expressionParser = ExpressionParser()

internal val v1_1StatementParsers: List<StatementParser> =
    run {
        val parsers = mutableListOf<StatementParser>()
        val blockParser = BlockParser(parsers)
        val conditionalParser = ConditionalParser(blockParser, expressionParser)
        parsers.addAll(v1_0StatementParsers)
        parsers.add(conditionalParser)
        parsers
    }

internal val v1_1StatementEvaluators: Map<ASTType, StatementEvaluator> =
    v1_0StatementEvaluators
