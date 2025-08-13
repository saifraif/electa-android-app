package bd.electa.app

import android.app.Application
import bd.electa.app.utils.TokenManager

class ElectaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}
