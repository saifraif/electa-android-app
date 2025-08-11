package bd.electa.app.models

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresInSeconds: Long? = null
)
