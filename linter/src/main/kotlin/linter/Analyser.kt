package linter

import ast.Program

class Analyser {
    fun analyse(program: Program, rulesConfig: RulesConfig): List<Warning>{
        val warnings = mutableListOf<Warning>()
        for(ast in program.trees)
            for (rule in rulesConfig.rules){
                val result = rule.apply(ast)
                if(result.isPresent) warnings.add(result.get())
            }
        return warnings
    }
}