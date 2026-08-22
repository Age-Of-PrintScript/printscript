package linter

import domain.Position

data class Warning(val message: String, val position: Position)