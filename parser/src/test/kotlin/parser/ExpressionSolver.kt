package parser

import ast.Expression
import domain.Either
import domain.Failure
import domain.Success
import domain.PrintScriptOperator
import domain.PrintScriptValue

internal class ExpressionSolver { //clase auxiliar para hacer tests mas faciles. Solo sirve para expresiones sin variables

    //TODO("terminar esto")

    //internal fun solve(
      //  expression: Expression,
        //values: Map<String, PrintScriptValue>
    //): Either<String, PrintScriptValue> {

      //  return when(expression){
        //    is Expression.Literal ->  {
          //      when(expression.value){
            //        is PrintScriptValue.NumberLiteral -> Success(expression.value)
              //      is PrintScriptValue.StringLiteral -> Success(expression.value)
                //}
            //}
            //is Expression.Variable -> values[expression.name]?.let { Success(it) }
              //  ?: Failure("variable is not defined")
            //is Expression.Operation -> solveOperation(expression, values)
            //}
    //}


    //private fun solveOperation(operation: Expression.Operation,
      //                         values :Map<String, PrintScriptValue>): Either<String, PrintScriptValue> {

        //falta resolver el caso de que si opero un string con un int

        //val left = solve(operation.left, values)
        //val right = solve(operation.right, values)

        //when (left) {
          //  is Success -> solveLiteral(left.value)
            //is Failure -> Failure(left.value)
        //}
        //when (right){
         //   is Success -> solveLiteral(right.value)
           // is Failure -> Failure(right.value)
            //}
       // }
    //}



    //private fun solveLiteral(value: PrintScriptValue): Either<String, PrintScriptValue> {
      //  return when (value) {

        //    //poco escalable, que pasa si le agrego mas tipos a literal? Chilla por todos lados pero te avisa
          //  is PrintScriptValue.NumberLiteral -> Success(value) //esto existe por ahora, debería llamar al caso del number literal
            //is PrintScriptValue.StringLiteral -> Success(value) //idem
        //}
    //}


}