package bd.electa.app.models

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String? = null
)
