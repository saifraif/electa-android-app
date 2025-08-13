package bd.electa.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private lateinit var appContext: Context

    private const val FILE_NAME = "secure_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    /** Call once from Application.onCreate() */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun prefs(): SharedPreferences {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            appContext,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /** Accepts nullable (your SessionManager calls nullable); null removes the key */
    fun saveAccessToken(token: String?) {
        if (token.isNullOrBlank()) {
            prefs().edit().remove(KEY_ACCESS_TOKEN).apply()
        } else {
            prefs().edit().putString(KEY_ACCESS_TOKEN, token).apply()
        }
    }

    fun saveRefreshToken(token: String?) {
        if (token.isNullOrBlank()) {
            prefs().edit().remove(KEY_REFRESH_TOKEN).apply()
        } else {
            prefs().edit().putString(KEY_REFRESH_TOKEN, token).apply()
        }
    }

    fun getAccessToken(): String? = prefs().getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs().getString(KEY_REFRESH_TOKEN, null)

    fun hasValidToken(): Boolean = !getAccessToken().isNullOrBlank()

    fun clear() {
        prefs().edit().clear().apply()
    }
}
