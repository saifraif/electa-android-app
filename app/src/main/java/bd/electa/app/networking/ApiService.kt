package bd.electa.app.networking

import bd.electa.app.data.AuthRequest
import bd.electa.app.data.EkycInitiateResponse
import bd.electa.app.data.TokenResponse
import bd.electa.app.data.CharterClause // Corrected import from 'data' package
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/api/v1/auth/register/request-otp")
    suspend fun requestOtp(@Body authRequest: AuthRequest): Response<Unit>

    @POST("/api/v1/auth/register/verify-otp")
    suspend fun verifyOtp(@Body authRequest: AuthRequest, @Query("otp") otp: Int): Response<TokenResponse>

    @POST("/api/v1/ekyc/initiate")
    suspend fun initiateEkycFlow(): Response<EkycInitiateResponse>

    @GET("/api/v1/charter/clauses")
    suspend fun getCharterClauses(): Response<List<CharterClause>>
}