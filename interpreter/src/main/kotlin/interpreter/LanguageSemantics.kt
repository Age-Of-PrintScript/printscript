package interpreter

data class LanguageSemantics(
    val functions: Map<String, BuiltInFunction>,
    val operations: Map<OperationKey, BinaryOperation>,
)
