package domain

interface PSType {
    val name: String
}

interface PSLiteral {
    val raw: String
    val type: PSType
}

interface PSOperator {
    val symbol: String
    val precedence: Int
}

interface PSFunction {
    val name: String
}
