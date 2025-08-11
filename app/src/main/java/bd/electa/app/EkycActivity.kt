package bd.electa.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                val res = RetrofitClient.api.initiateEkycFlow()
                if (res.isSuccessful) {
                    val url = res.body()?.redirectUrl
                    if (!url.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@EkycActivity, "No redirect URL", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EkycActivity, "eKYC failed: ${res.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EkycActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBarEkyc.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnStartEkyc.isEnabled = !loading
    }
}
