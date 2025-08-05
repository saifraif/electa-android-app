package bd.electa.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bd.electa.app.data.AuthRequest
import bd.electa.app.networking.RetrofitClient
import bd.electa.app.utils.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(applicationContext)

        val etMobileNumber = findViewById<EditText>(R.id.etMobileNumber)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etOtp = findViewById<EditText>(R.id.etOtp)
        val btnRequestOtp = findViewById<Button>(R.id.btnRequestOtp)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val btnGoToEkyc = findViewById<Button>(R.id.btnGoToEkyc)

        btnGoToEkyc.setOnClickListener {
            val intent = Intent(this, EkycActivity::class.java)
            startActivity(intent)
        }

        btnRequestOtp.setOnClickListener {
            val mobile = etMobileNumber.text.toString()
            val password = etPassword.text.toString()

            if (mobile.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter mobile and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.requestOtp(AuthRequest(mobile, password))
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "OTP requested! Check backend logs.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Network Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                }
            }
        }

        btnVerify.setOnClickListener {
            val mobile = etMobileNumber.text.toString()
            val password = etPassword.text.toString()
            val otp = etOtp.text.toString().toIntOrNull()

            if (mobile.isBlank() || password.isBlank() || otp == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.verifyOtp(AuthRequest(mobile, password), otp)
                    if (response.isSuccessful) {
                        val token = response.body()?.accessToken
                        if (token != null) {
                            // HERE IS THE FIX: We now use the 'token' variable
                            sessionManager.saveAuthToken(token)
                            Toast.makeText(this@MainActivity, "Success! Token has been securely saved.", Toast.LENGTH_LONG).show()
                            // Now that registration is done, let's go to the e-KYC screen
                            val intent = Intent(this@MainActivity, EkycActivity::class.java)
                            startActivity(intent)
                            finish() // Close the registration screen
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Verification Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Network Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}