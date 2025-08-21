package bd.electa.app

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bd.electa.app.databinding.ActivityLoginBinding
import bd.electa.app.models.LoginRequest
import bd.electa.app.networking.RetrofitClient
import bd.electa.app.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // If token already exists, go straight to Main
        sessionManager.getAccessToken()?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.loginButton?.setOnClickListener {
            val email = binding.emailEditText?.text?.toString()?.trim().orEmpty()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            requestOtp(email)
        }
    }

    private fun requestOtp(email: String) {
        setLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp = RetrofitClient.instance.requestOtp(LoginRequest(email = email))
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        Toast.makeText(
                            this@LoginActivity,
                            "OTP sent. Please check your email.",
                            Toast.LENGTH_LONG
                        ).show()
                        promptAndVerify(email)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Failed to send OTP: ${resp.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { showNetworkError(e) }
            } finally {
                withContext(Dispatchers.Main) { setLoading(false) }
            }
        }
    }

    private fun promptAndVerify(email: String) {
        val editText = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Enter OTP"
        }
        AlertDialog.Builder(this)
            .setTitle("Verify OTP")
            .setView(editText)
            .setPositiveButton("Verify") { dlg, _ ->
                val otpText = editText.text?.toString()?.trim()
                val otp = otpText?.toIntOrNull()
                if (otp == null) {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                } else {
                    verifyOtp(email, otp)
                }
                dlg.dismiss()
            }
            .setNegativeButton("Cancel") { dlg, _ -> dlg.dismiss() }
            .show()
    }

    private fun verifyOtp(email: String, otp: Int) {
        setLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resp = RetrofitClient.instance.verifyOtp(LoginRequest(email = email), otp)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        val body = resp.body()
                        // Prefer FastAPI-style "access_token". If your backend returns "accessToken", switch to that:
                        val token = body?.access_token /* or body?.accessToken */
                        if (!token.isNullOrBlank()) {
                            sessionManager.saveAccessToken(token)
                            Toast.makeText(this@LoginActivity, "Logged in", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Empty token in response",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Verify failed: ${resp.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { showNetworkError(e) }
            } finally {
                withContext(Dispatchers.Main) { setLoading(false) }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar?.visibility = if (loading) View.VISIBLE else View.GONE
        binding.loginButton?.isEnabled = !loading
    }

    private fun showNetworkError(e: Exception) {
        val msg = when (e) {
            is HttpException -> "HTTP ${e.code()}"
            else -> e.localizedMessage ?: "Unknown error"
        }
        Toast.makeText(this, "Network error: $msg", Toast.LENGTH_LONG).show()
    }
}
