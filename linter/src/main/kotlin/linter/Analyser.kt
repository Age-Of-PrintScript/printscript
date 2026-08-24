package linter

import ast.AST
import ast.Program

class Analyser {
    fun analyse(program: Program, rulesConfig: RulesConfig): List<Warning>{
        val warnings = mutableListOf<Warning>()
        for(ast in program.trees)
            warnings += checkRules(ast, rulesConfig)
        return warnings
    }

    fun checkRules(ast: AST, rulesConfig: RulesConfig): List<Warning>{
        val warnings = mutableListOf<Warning>()
        for (rule in rulesConfig.rules){
            val result = rule.apply(ast)
            if(result != null) warnings.add(result)
        }
        return warnings
    }

}