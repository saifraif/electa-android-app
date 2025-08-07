package com.electa.api

import com.electa.models.LoginRequest
import com.electa.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}