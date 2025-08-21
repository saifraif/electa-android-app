package bd.electa.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private lateinit var appContext: Context
    private var cachedPrefs: SharedPreferences? = null

    private const val FILE_NAME = "secure_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    /** Call once from Application.onCreate() */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun prefs(): SharedPreferences {
        return cachedPrefs ?: run {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                appContext,
                FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ).also { cachedPrefs = it }
        }
    }

    fun saveAccessToken(token: String?) {
        prefs().edit().apply {
            if (token.isNullOrBlank()) remove(KEY_ACCESS_TOKEN)
            else putString(KEY_ACCESS_TOKEN, token)
        }.apply()
    }

    fun saveRefreshToken(token: String?) {
        prefs().edit().apply {
            if (token.isNullOrBlank()) remove(KEY_REFRESH_TOKEN)
            else putString(KEY_REFRESH_TOKEN, token)
        }.apply()
    }

    fun getAccessToken(): String? = prefs().getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs().getString(KEY_REFRESH_TOKEN, null)
    fun hasValidToken(): Boolean = !getAccessToken().isNullOrBlank()

    fun clear() {
        prefs().edit().clear().apply()
    }
}
