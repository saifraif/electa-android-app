package com.electa.api

import com.electa.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Uses the URL from your build config
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}