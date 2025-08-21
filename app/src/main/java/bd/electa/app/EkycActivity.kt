package bd.electa.app

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import bd.electa.app.databinding.ActivityEkycBinding
import bd.electa.app.networking.RetrofitClient
import kotlinx.coroutines.launch

class EkycActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEkycBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEkycBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartEkyc.setOnClickListener { startEkyc() }
    }

    private fun startEkyc() {
        setLoading(true)

        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.initiateEkycFlow()

                if (resp.isSuccessful) {
                    val url = resp.body()?.redirectUrl?.takeIf { !it.isNullOrBlank() }
                    if (url != null) {
                        openInCustomTabs(url)
                    } else {
                        showToast("Invalid redirect URL")
                    }
                } else {
                    showToast("Failed: ${resp.code()} ${resp.message()}")
                }
            } catch (e: Exception) {
                showToast("Network error: ${e.localizedMessage}")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun openInCustomTabs(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this, Uri.parse(url))
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBarEkyc.isVisible = loading
        binding.btnStartEkyc.isEnabled = !loading
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
