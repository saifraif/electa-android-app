package bd.electa.app.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // IMPORTANT: 10.0.2.2 is a special IP address that allows the Android emulator
    // to connect to the 'localhost' of the computer it is running on.
    // This is how our app will talk to the Docker backend.
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}