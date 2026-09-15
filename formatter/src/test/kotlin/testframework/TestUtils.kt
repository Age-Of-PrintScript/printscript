package testframework

import ast.AST
import ast.Block
import ast.Expression
import domain.NumType
import domain.PSType
import domain.StrType
import domain.getOrReturn
import formatter.ConfigProvider
import formatter.formatrules.EnsureNoSpaceAroundEquals
import formatter.formatrules.EnsureSingleSpace
import formatter.formatrules.EnsureSpaceAfterColon
import formatter.formatrules.EnsureSpaceAroundEquals
import formatter.formatrules.EnsureSpaceBeforeColon
import formatter.formatrules.EnsureSpacesSurroundingOperations
import formatter.formatrules.FormatRules
import formatter.formatrules.IfBraceBelowLine
import formatter.formatrules.IfBraceSameLine
import formatter.formatrules.IndentsInsideIf
import formatter.formatrules.LineBreaksAfterPrintLn
import formatter.formattokens.AssignmentFormatTokenizer
import formatter.formattokens.ConditionalFormatTokenizer
import formatter.formattokens.DeclarationFormatTokenizer
import formatter.formattokens.ExpressionFormatTokenizer
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

fun createDefaultConfig10(): ConfigProvider {
    val defaultRules =
        FormatRules(
            listOf(
                EnsureSpacesSurroundingOperations(false),
                EnsureSingleSpace(false),
                EnsureSpaceAroundEquals(false),
                EnsureNoSpaceAroundEquals(false),
                EnsureSpaceBeforeColon(false),
                EnsureSpaceAfterColon(false),
                LineBreaksAfterPrintLn(0),
            ),
        )
    return ConfigProvider(
        mapOf(
            DeclarationFormatTokenizer() to defaultRules,
            AssignmentFormatTokenizer() to defaultRules,
            ExpressionFormatTokenizer() to defaultRules,
        ),
    )
}

fun createDefaultConfig11(): ConfigProvider {
    val defaultRules =
        FormatRules(
            listOf(
                EnsureSpacesSurroundingOperations(false),
                EnsureSingleSpace(false),
                EnsureSpaceAroundEquals(false),
                EnsureNoSpaceAroundEquals(false),
                EnsureSpaceBeforeColon(false),
                EnsureSpaceAfterColon(false),
                IfBraceSameLine(false),
                IfBraceBelowLine(false),
                IndentsInsideIf(0),
                LineBreaksAfterPrintLn(0),
            ),
        )
    val stmts =
        mutableSetOf<FormatTokenizer>(
            DeclarationFormatTokenizer(),
            AssignmentFormatTokenizer(),
            ExpressionFormatTokenizer(),
        )
    val conditionalTokenizer = ConditionalFormatTokenizer(stmts)
    stmts.add(conditionalTokenizer)

    return ConfigProvider(
        mapOf(
            DeclarationFormatTokenizer() to defaultRules,
            AssignmentFormatTokenizer() to defaultRules,
            ExpressionFormatTokenizer() to defaultRules,
            conditionalTokenizer to defaultRules,
        ),
    )
}
