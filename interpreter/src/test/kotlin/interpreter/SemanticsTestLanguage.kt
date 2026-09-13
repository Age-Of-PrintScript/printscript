package interpreter

import ast.ASTType
import domain.BoolType
import domain.Failure
import domain.NumType
import domain.PSLiteral
import domain.PSOperator
import domain.StrType
import domain.Success
import interpreter.statement.AssignmentEvaluator
import interpreter.statement.DeclarationEvaluator
import interpreter.statement.ExpressionStatementEvaluator
import interpreter.statement.StatementEvaluator

val printlnFunction =
    BuiltInFunction { args, io ->
        val message = if (args.isNotEmpty()) args.first().raw else ""
        io.emitter.print(message)
        Success(null)
    }

val readInputFunction =
    BuiltInFunction { args, io ->
        val prompt = if (args.isNotEmpty()) args.first().raw else ""
        val input = io.provider.readInput(prompt)
        Success(PSLiteral(input, StrType))
    }

val testBuiltInFunctions: Map<String, BuiltInFunction> =
    mapOf(
        "println" to printlnFunction,
        "readInput" to readInputFunction,
    )

enum class Operators(
    override val symbol: String,
    override val precedence: Int,
) : PSOperator {
    SUM("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
}

val sumNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() + r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val sumStrAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val sumStrAndNum =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val sumNumAndStr =
    BinaryOperation { l, r ->
        Success(PSLiteral(l.raw + r.raw, StrType))
    }

val subNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() - r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val multNumAndNum =
    BinaryOperation { l, r ->
        val result = l.raw.toDouble() * r.raw.toDouble()
        Success(PSLiteral(result.toString(), NumType))
    }

val divNumAndNum =
    BinaryOperation { l, r ->
        val rightVal = r.raw.toDouble()
        if (rightVal == 0.0) {
            Failure(RuntimeError.MATH_ERROR)
        } else {
            val result = l.raw.toDouble() / rightVal
            Success(PSLiteral(result.toString(), NumType))
        }
    }

val multNumAndString =
    BinaryOperation { l, r ->
        val times =
            l.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)
        Success(PSLiteral(r.raw.repeat(times.toInt()), StrType))
    }

val multStringAndNum =
    BinaryOperation { l, r ->
        val times =
            r.raw
                .toDoubleOrNull()
                ?.takeIf { it % 1.0 == 0.0 && it >= 0.0 }
                ?: return@BinaryOperation Failure(RuntimeError.STRING_REPETITION_REQUIRES_INT)

        Success(PSLiteral(l.raw.repeat(times.toInt()), StrType))
    }

val testBinaryOperations: Map<OperationKey, BinaryOperation> =
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

val testStatementEvaluators: Map<ASTType, StatementEvaluator> =
    mapOf(
        ASTType.DECLARATION to DeclarationEvaluator(),
        ASTType.ASSIGNMENT to AssignmentEvaluator(),
        ASTType.EXPRESSION_STATEMENT to ExpressionStatementEvaluator(),
    )

val testStrToNum =
    TypeCast { value ->
        val num =
            value.raw.toDoubleOrNull()
                ?: return@TypeCast Failure(RuntimeError.INVALID_CAST)
        Success(PSLiteral(num.toString(), NumType))
    }

val testStrToBool =
    TypeCast { value ->
        val bool =
            value.raw.toBooleanStrictOrNull()
                ?: return@TypeCast Failure(RuntimeError.INVALID_CAST)
        Success(PSLiteral(bool.toString(), BoolType))
    }

val testTypeCasters: Map<CastKey, TypeCast> =
    mapOf(
        CastKey(StrType, NumType) to testStrToNum,
        CastKey(StrType, BoolType) to testStrToBool,
    )

val testSemantics: LanguageSemantics =
    LanguageSemantics(
        functions = testBuiltInFunctions,
        operations = testBinaryOperations,
        statementEvaluators = testStatementEvaluators,
        typeCasters = testTypeCasters,
    )
