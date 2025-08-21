package bd.electa.app.networking

import bd.electa.app.models.LoginRequest
import bd.electa.app.models.LoginResponse
import bd.electa.app.models.EkycInitiateResponse
import bd.electa.app.models.CharterClause
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/api/v1/auth/register/request-otp")
    suspend fun requestOtp(@Body authRequest: LoginRequest): Response<Unit>

    @POST("/api/v1/auth/register/verify-otp")
    suspend fun verifyOtp(
        @Body authRequest: LoginRequest,
        @Query("otp") otp: Int
    ): Response<LoginResponse>

    @POST("/api/v1/ekyc/initiate")
    suspend fun initiateEkycFlow(): Response<EkycInitiateResponse>

    @GET("/api/v1/charter/clauses")
    suspend fun getCharterClauses(): Response<List<CharterClause>>
}
