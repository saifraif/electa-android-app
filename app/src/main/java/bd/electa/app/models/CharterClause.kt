package bd.electa.app.models

/**
 * Canonical CharterClause model used across networking and UI.
 * Keep fields nullable + defaulted so Retrofit/Moshi/Gson can parse
 * even if the backend omits something.
 */
data class CharterClause(
    val id: Int? = null,
    val group: String? = null,       // e.g., "A", "B" or a group label
    val number: String? = null,      // e.g., "1.1", "02", etc.
    val title: String? = null,       // short heading
    val description: String? = null  // full text/body (if provided)
)
