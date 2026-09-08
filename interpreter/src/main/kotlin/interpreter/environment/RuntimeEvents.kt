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

    operator fun plus(newEvents: List<Event>): RuntimeEvents = RuntimeEvents(events + newEvents)
    operator fun plus(newEvent: Event): RuntimeEvents = RuntimeEvents(events + newEvent)
}
