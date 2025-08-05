package bd.electa.app.data

import com.google.gson.annotations.SerializedName

// Data class for the /request-otp and /verify-otp request body
data class AuthRequest(
    val mobile_number: String,
    val password: String
)

// Data class for the token response from /verify-otp
data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)