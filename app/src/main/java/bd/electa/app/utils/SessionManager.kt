package bd.electa.app.utils

import android.content.Context
import bd.electa.app.utils.TokenManager


class SessionManager(private val context: Context) {
    fun saveAccessToken(token: String?) = TokenManager.saveAccessToken(token)
    fun saveRefreshToken(token: String?) = TokenManager.saveRefreshToken(token)
    fun getAccessToken(): String? = TokenManager.getAccessToken()
    fun getRefreshToken(): String? = TokenManager.getRefreshToken()
    fun hasValidToken(): Boolean = TokenManager.hasValidToken()
    fun clear() = TokenManager.clear()
}
