package versionfactory.v1_0

import domain.PSOperator

enum class Operators(
    override val symbol: String,
    override val precedence: Int,
) : PSOperator {
    SUM("+", 1),
    SUBTRACT("-", 1),
    MULTIPLY("*", 2),
    DIVIDE("/", 2),
}
