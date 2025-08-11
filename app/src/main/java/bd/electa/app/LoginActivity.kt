package bd.electa.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bd.electa.app.databinding.ActivityLoginBinding
import bd.electa.app.models.AuthRequest
import bd.electa.app.networking.RetrofitClient
import bd.electa.app.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text?.toString().orEmpty()
            val password = binding.passwordEditText.text?.toString().orEmpty()

            if (email.isBlank()) {
                binding.emailInputLayout.error = "Email required"
                return@setOnClickListener
            } else binding.emailInputLayout.error = null

            if (password.isBlank()) {
                binding.passwordInputLayout.error = "Password required"
                return@setOnClickListener
            } else binding.passwordInputLayout.error = null

            doLogin(email, password)
        }
    }

    private fun doLogin(email: String, password: String) {
        setLoading(true)
        lifecycleScope.launch {
            try {
                // demo flow: request OTP then verify with a hardcoded otp=1234
                val req = AuthRequest(email, password)
                val reqRes = RetrofitClient.api.requestOtp(req)
                if (!reqRes.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "OTP request failed", Toast.LENGTH_SHORT).show()
                    setLoading(false); return@launch
                }
                val verifyRes = RetrofitClient.api.verifyOtp(req, otp = 1234)
                if (verifyRes.isSuccessful) {
                    val token = verifyRes.body()?.accessToken
                    if (!token.isNullOrBlank()) {
                        session.setToken(token)
                        Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "No token received", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Verify failed: ${verifyRes.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !loading
    }
}
