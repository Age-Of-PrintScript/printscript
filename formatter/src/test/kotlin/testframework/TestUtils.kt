package testframework

import ast.AST
import ast.Block
import ast.Expression
import domain.NumType
import domain.PSType
import domain.StrType
import domain.getOrReturn
import formatter.formattokens.FormatTokenizer
import formatter.formattokens.FormatTokens

fun createDeclaration(
    name: String,
    type: PSType = StrType,
    value: Expression? = createStringLiteralExpression("test"),
): AST.DeclarationStatement =
    AST.DeclarationStatement(
        id = name,
        type = type,
        mutable = true,
        value = value,
    )

fun createAssignment(
    name: String,
    value: Expression = createNumberLiteralExpression(1),
): AST.AssignmentStatement =
    AST.AssignmentStatement(
        id = name,
        value = value,
    )

fun createPrintln(vararg args: Expression): AST.ExpressionStatement =
    AST.ExpressionStatement(
        expression =
            Expression.Call(
                name = "println",
                args = args.toList(),
            ),
    )

fun createNumberLiteralExpression(value: Number): Expression.Literal = Expression.Literal(value.toString(), NumType)

fun createStringLiteralExpression(value: String): Expression.Literal = Expression.Literal(value, StrType)

fun createVariableExpression(name: String): Expression.Variable = Expression.Variable(name)

fun createConditional(
    condition: Expression = createVariableExpression("x"),
    ifBlock: List<AST> = listOf(createPrintln(createStringLiteralExpression("hi"))),
    elseBlock: List<AST>? = null,
): AST.ConditionalStatement =
    AST.ConditionalStatement(
        condition = condition,
        ifBlock = Block(ifBlock),
        elseBlock = elseBlock?.let { Block(it) },
    )

fun tokensFrom(
    tokenizer: FormatTokenizer,
    ast: AST,
): FormatTokens = tokenizer.tokenize(ast).getOrReturn { error("tokenizer fallo: $it") }
