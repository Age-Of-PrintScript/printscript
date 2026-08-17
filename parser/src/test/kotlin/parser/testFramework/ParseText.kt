package parser.testFramework

internal fun splitIntoSections(text: String): Map<String, List<String>> {
    val sections = mutableMapOf<String, MutableList<String>>()
    var currentHeader: String? = null

    for (line in text.lines()) {
        if (line.startsWith("#")) {
            currentHeader = line.removePrefix("#").trim()
            sections[currentHeader] = mutableListOf()
        } else if (currentHeader != null && line.isNotBlank()) {
            sections.getValue(currentHeader).add(line)
        }
    }

    return sections.toMap()
}