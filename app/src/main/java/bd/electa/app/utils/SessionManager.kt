package bd.electa.app.utils

import android.content.Context

class SessionManager(context: Context) {
    private val tokenManager = TokenManager(context.applicationContext)

    fun hasValidToken(): Boolean = !tokenManager.getAccessToken().isNullOrBlank()

    fun setToken(token: String) = tokenManager.saveAccessToken(token)

    fun clear() = tokenManager.clear()
}
