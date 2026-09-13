package domain

interface PSType {
    val name: String
}

object BoolType : PSType {
    override val name = "boolean"
}

object StrType : PSType {
    override val name = "string"
}

object NumType : PSType {
    override val name = "number"
}

data class PSLiteral(
    val raw: String,
    val type: PSType,
)

interface PSOperator {
    val symbol: String
    val precedence: Int
}

interface PSFunction {
    val name: String
}
