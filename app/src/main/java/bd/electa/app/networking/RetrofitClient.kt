package bd.electa.app.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // If you set BASE_URL in BuildConfig, great. Otherwise, hardcode it here:
    private const val FALLBACK_BASE_URL = "https://example.com"

    private val logging by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttp by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        val baseUrl = try {
            // Use BuildConfig if generated
            val field = Class.forName("bd.electa.app.BuildConfig").getField("BASE_URL")
            (field.get(null) as? String)?.takeIf { it.isNotBlank() } ?: FALLBACK_BASE_URL
        } catch (_: Throwable) {
            FALLBACK_BASE_URL
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
