package com.electa.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ApiClient(private val token: String?) {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(token))
        .build()

    fun newCall(request: Request): okhttp3.Call {
        return client.newCall(request)
    }

    inner class AuthInterceptor(private val token: String?) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            return if (token != null) {
                chain.proceed(
                    original.newBuilder()
                        .header("x-auth-token", token)
                        .build()
                )
            } else {
                chain.proceed(original)
            }
        }
    }
}