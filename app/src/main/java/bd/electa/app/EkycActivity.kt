package bd.electa.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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

        binding.btnStartEkyc.setOnClickListener {
            initiateEkyc()
        }
    }

    private fun initiateEkyc() {
        binding.progressBarEkyc.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.initiateEkycFlow()
                if (response.isSuccessful) {
                    val redirectUrl = response.body()?.redirectUrl
                    Toast.makeText(this@EkycActivity, "Redirecting to mock bank...", Toast.LENGTH_LONG).show()

                    if (!redirectUrl.isNullOrEmpty()) {
                        // In a real app, this would open a secure in-app browser (Chrome Custom Tab).
                        // For this test, we'll just open the standard browser.
                        val browserIntent = Intent(Intent.ACTION_VIEW, redirectUrl.toUri())
                        startActivity(browserIntent)
                    }

                } else {
                    Toast.makeText(this@EkycActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EkycActivity, "Network Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBarEkyc.visibility = View.GONE
            }
        }
    }
}