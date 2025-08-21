package bd.electa.app.auth

import bd.electa.app.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Legacy OkHttp login helper kept for compatibility.
 * Prefer using your Retrofit API going forward, but this is now aligned with the FastAPI backend.
 *
 * - Base URL comes from BuildConfig.BASE_URL (set in build.gradle.kts)
 * - Endpoint: /api/v1/admin/login
 * - Request: application/x-www-form-urlencoded with username/password
 * - Response: expects { "access_token": "<JWT>" }
 */
class AuthService {
    private val client = OkHttpClient()

    // We will send form-encoded data (FastAPI OAuth2-style)
    private val formMediaType = "application/x-www-form-urlencoded".toMediaType()

    // Ensure baseUrl ends with a slash
    private val baseUrl = if (BuildConfig.BASE_URL.endsWith("/")) {
        BuildConfig.BASE_URL
    } else {
        BuildConfig.BASE_URL + "/"
    }

    fun login(email: String, password: String, callback: (String?, Exception?) -> Unit) {
        // FastAPI typically expects: username=<email>&password=<password>
        val form = "username=${encode(email)}&password=${encode(password)}"
            .toRequestBody(formMediaType)

        val request = Request.Builder()
            .url(baseUrl + "api/v1/admin/login")
            .post(form)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { resp ->
                    if (!resp.isSuccessful) {
                        callback(
                            null,
                            IOException("HTTP ${resp.code}: ${resp.message}")
                        )
                        return
                    }
                    try {
                        val bodyStr = resp.body?.string() ?: ""
                        val json = JSONObject(bodyStr)
                        // FastAPI convention is "access_token"
                        val token = when {
                            json.has("access_token") -> json.optString("access_token", null)
                            json.has("accessToken") -> json.optString("accessToken", null) // fallback
                            else -> null
                        }
                        if (token.isNullOrBlank()) {
                            callback(null, IllegalStateException("Token missing in response"))
                        } else {
                            callback(token, null)
                        }
                    } catch (e: Exception) {
                        callback(null, e)
                    }
                }
            }
        })
    }

    // Basic URL encoding for form fields
    private fun encode(s: String): String =
        java.net.URLEncoder.encode(s, Charsets.UTF_8.name())
}
