package versionfactory

enum class VersionError(
    val message: String,
) {
    VERSION_DOESNT_EXISTS("that version doesn't exists"),
}
