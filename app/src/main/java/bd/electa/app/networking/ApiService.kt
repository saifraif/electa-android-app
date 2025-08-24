package bd.electa.app.networking

import bd.electa.app.models.CharterClause
import bd.electa.app.models.EkycInitiateResponse
import bd.electa.app.models.LoginRequest
import bd.electa.app.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// ---------- DTOs for public browsing ----------
data class Paged<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val total: Int
)

data class Party(
    val id: String? = null,
    val name: String,
    val abbrev: String? = null,
    val logoUrl: String? = null,
    val description: String? = null
)

data class Candidate(
    val id: String? = null,
    val fullName: String,
    val partyName: String? = null,
    val constituencyName: String? = null,
    val photoUrl: String? = null,
    val bio: String? = null
)

// ---------- Auth payload for combined OTP verify ----------
data class VerifyOtpPayload(
    val mobile_number: String,
    val password: String,
    val otp: Int
)

interface ApiService {

    // ===== Auth (MVP) =====

    // POST /api/v1/auth/register/request-otp
    // expects { mobile_number, password }
    @POST("/api/v1/auth/register/request-otp")
    suspend fun requestOtp(@Body authRequest: LoginRequest): Response<Unit>

    // New: backend expects JSON body { mobile_number, password, otp }
    @POST("/api/v1/auth/register/verify-otp")
    suspend fun verifyOtp(@Body payload: VerifyOtpPayload): Response<LoginResponse>

    @POST("/api/v1/ekyc/initiate")
    suspend fun initiateEkycFlow(): Response<EkycInitiateResponse>

    @GET("/api/v1/charter/clauses")
    suspend fun getCharterClauses(): Response<List<CharterClause>>

    // ===== Public browse (no auth) =====
    @GET("/api/v1/public/parties")
    suspend fun getParties(
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<Paged<Party>>

    @GET("/api/v1/public/candidates")
    suspend fun getCandidates(
        @Query("party") party: String? = null,
        @Query("constituency") constituency: String? = null,
        @Query("q") q: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<Paged<Candidate>>
}
