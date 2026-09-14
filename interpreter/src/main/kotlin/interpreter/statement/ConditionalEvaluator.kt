package interpreter.statement

import ast.AST
import domain.BoolType
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import interpreter.InterpreterIO
import interpreter.LanguageSemantics
import interpreter.RuntimeError
import interpreter.environment.RuntimeEnvironment
import interpreter.executeBlock

class ConditionalEvaluator : StatementEvaluator {
    override fun evaluate(
        statement: AST,
        env: RuntimeEnvironment,
        io: InterpreterIO,
        semantics: LanguageSemantics,
    ): Either<RuntimeError, RuntimeEnvironment> {
        val conditional = statement as AST.ConditionalStatement

        val condition =
            solveExpression(conditional.condition, env, io, semantics)
                .getOrReturn { return Failure(it) }
                ?: return Failure(RuntimeError.MISSING_IF_CONDITION)
        val type = condition.type
        if (type !is BoolType) return Failure(RuntimeError.MISSING_IF_CONDITION)

        val blockToExecute =
            if (type.isTrue(condition.raw)) {
                conditional.ifBlock
            } else {
                conditional.elseBlock ?: return Success(env)
            }

        return executeBlock(blockToExecute.statements, env, io, semantics)
    }
}
