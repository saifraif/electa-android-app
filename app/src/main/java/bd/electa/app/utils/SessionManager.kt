package bd.electa.app.utils

import android.content.Context

/**
 * Thin wrapper around TokenManager to keep a stable constructor signature.
 * Pass in Context to match existing call sites, though storage is handled by TokenManager.
 */
class SessionManager(@Suppress("UNUSED_PARAMETER") private val context: Context) {
    fun saveAccessToken(token: String?) = TokenManager.saveAccessToken(token)
    fun saveRefreshToken(token: String?) = TokenManager.saveRefreshToken(token)
    fun getAccessToken(): String? = TokenManager.getAccessToken()
    fun getRefreshToken(): String? = TokenManager.getRefreshToken()
    fun hasValidToken(): Boolean = TokenManager.hasValidToken()
    fun clear() = TokenManager.clear()
}
