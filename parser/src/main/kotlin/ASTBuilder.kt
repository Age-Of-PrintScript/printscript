internal sealed interface ASTBuilder {
    fun build(): Either<SINTAX_ERROR, AST>

    data class DeclarationBuilder(
        val id: Identifier? = null,
        val type: DataType? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SINTAX_ERROR, AST> {
            val safeId = id ?: return Failure(SINTAX_ERROR("Missing identifier in declaration"))
            val safeType = type ?: return Failure(SINTAX_ERROR("Missing type in declaration"))
            return Success(AST.Declaration(safeId, safeType, value))
        }
    }

    data class AssignmentBuilder(
        val id: Identifier? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SINTAX_ERROR, AST> {
            val safeId = id ?: return Failure(SINTAX_ERROR("Missing identifier in assignment"))
            val safeValue = value ?: return Failure(SINTAX_ERROR("Missing value in assignment"))
            return Success(AST.Assignment(safeId, safeValue))
        }
    }

    data class CallBuilder(
        val functionName: String? = null
    ) : ASTBuilder {
        override fun build(): Either<SINTAX_ERROR, AST> {
            val safeName = functionName ?: return Failure(SINTAX_ERROR("Missing function name in call"))
            return Success(AST.Call(safeName))
        }
    }
}