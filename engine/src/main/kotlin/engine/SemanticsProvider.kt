package engine

import engine.ps_versions.v1_0.v1_0BinaryOperations
import engine.ps_versions.v1_0.v1_0builtInFunctions
import interpreter.LanguageSemantics

val v1_0semantics: LanguageSemantics = LanguageSemantics(v1_0builtInFunctions, v1_0BinaryOperations)
