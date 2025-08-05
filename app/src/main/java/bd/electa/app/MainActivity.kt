package bd.electa.app

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
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMobileNumber = findViewById<EditText>(R.id.etMobileNumber)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etOtp = findViewById<EditText>(R.id.etOtp)
        val btnRequestOtp = findViewById<Button>(R.id.btnRequestOtp)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

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
                        Toast.makeText(this@MainActivity, "Success! Token received.", Toast.LENGTH_LONG).show()
                        // In a real app, we would now save this token securely and navigate to the home screen.
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