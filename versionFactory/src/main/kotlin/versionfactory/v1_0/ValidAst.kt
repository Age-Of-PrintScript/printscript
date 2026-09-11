package versionfactory.v1_0

import ast.ASTType
import interpreter.statement.AssignmentEvaluator
import interpreter.statement.DeclarationEvaluator
import interpreter.statement.ExpressionStatementEvaluator
import interpreter.statement.StatementEvaluator
import parser.BlockParser
import parser.ExpressionParser
import parser.builders.AssignmentParser
import parser.builders.DeclarationParser
import parser.builders.ExpressionStatementParser

internal val expressionParser = ExpressionParser()

internal val v1_0BlockParser: BlockParser =
    BlockParser(
        listOf(
            DeclarationParser(expressionParser),
            AssignmentParser(expressionParser),
            ExpressionStatementParser(expressionParser),
        ),
    )

internal val v1_0StatementEvaluators: Map<ASTType, StatementEvaluator> =
    mapOf(
        ASTType.ASSIGNMENT to AssignmentEvaluator(),
        ASTType.DECLARATION to DeclarationEvaluator(),
        ASTType.EXPRESSION_STATEMENT to ExpressionStatementEvaluator(),
    )
