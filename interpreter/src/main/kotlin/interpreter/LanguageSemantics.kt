package interpreter

import ast.ASTType
import interpreter.statement.StatementEvaluator

data class LanguageSemantics(
    val functions: Map<String, BuiltInFunction>,
    val operations: Map<OperationKey, BinaryOperation>,
    val statementEvaluators: Map<ASTType, StatementEvaluator>,
)
