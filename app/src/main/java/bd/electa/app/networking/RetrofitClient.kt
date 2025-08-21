package bd.electa.app.networking

import bd.electa.app.BuildConfig
import bd.electa.app.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Interceptor to attach Authorization header if a token exists
    private val authHeaderInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()

        TokenManager.getAccessToken()?.let { token ->
            if (token.isNotBlank()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
        }

        chain.proceed(builder.build())
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Optional: handle 401 by clearing the token (uncomment to enable)
    private val unauthorizedInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            // TokenManager.clear() // implement this if you want auto-logout
            // You could also broadcast an event or use a callback to navigate to Login
        }
        response
    }

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authHeaderInterceptor)
            .addInterceptor(logging)
            .addInterceptor(unauthorizedInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Ensure Retrofit baseUrl ends with a slash
    private val baseUrl: String by lazy {
        if (BuildConfig.BASE_URL.endsWith("/")) BuildConfig.BASE_URL
        else BuildConfig.BASE_URL + "/"
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
