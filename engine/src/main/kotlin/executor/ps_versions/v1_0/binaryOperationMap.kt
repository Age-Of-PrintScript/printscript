package executor.ps_versions.v1_0

import domain.NumType
import domain.StrType
import interpreter.BinaryOperation
import interpreter.OperationKey

val v1_0binaryOperations: Map<OperationKey, BinaryOperation> =
    mapOf(
        OperationKey(Operators.SUM, NumType, NumType) to sumNumAndNum,
        OperationKey(Operators.SUM, StrType, StrType) to sumStrAndStr,
        OperationKey(Operators.SUM, StrType, NumType) to sumStrAndNum,
        OperationKey(Operators.SUM, NumType, StrType) to sumNumAndStr,
        OperationKey(Operators.SUBTRACT, NumType, NumType) to subNumAndNum,
        OperationKey(Operators.MULTIPLY, NumType, NumType) to multNumAndNum,
        OperationKey(Operators.MULTIPLY, NumType, StrType) to multNumAndString,
        OperationKey(Operators.MULTIPLY, StrType, NumType) to multStringAndNum,
        OperationKey(Operators.DIVIDE, NumType, NumType) to divNumAndNum,
    )
