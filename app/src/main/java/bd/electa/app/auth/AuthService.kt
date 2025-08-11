package bd.electa.app.auth

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthService {
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json".toMediaType()
    private val baseUrl = "http://10.0.2.2:5000/api/auth" // Use 10.0.2.2 for Android emulator

    fun login(email: String, password: String, callback: (String?, Exception?) -> Unit) {
        val body = JSONObject()
            .put("email", email)
            .put("password", password)
            .toString()
            .toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("$baseUrl/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val token = JSONObject(response.body?.string()).getString("token")
                    callback(token, null)
                } catch (e: Exception) {
                    callback(null, e)
                }
            }
        })
    }
}