package interpreter.environment

data class RuntimeEvents(
    val events: List<Event>,
) {
    fun addEvent(event: Event): RuntimeEvents =
        RuntimeEvents(
            events
                .toMutableList()
                .apply {
                    add(event)
                }.toList(),
        )
}
