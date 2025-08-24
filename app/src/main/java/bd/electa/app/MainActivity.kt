package bd.electa.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bd.electa.app.databinding.ActivityMainBinding
import bd.electa.app.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Charter (public content)
        binding.btnOpenCharter?.setOnClickListener {
            startActivity(Intent(this, CharterActivity::class.java))
        }

        // eKYC (optional for later)
        binding.btnOpenEkyc?.setOnClickListener {
            startActivity(Intent(this, EkycActivity::class.java))
        }

        // Login (optional for later)
        binding.btnOpenLogin?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // NEW: guest browsing — open Parties list
        binding.btnOpenPublicParties?.setOnClickListener {
            startActivity(Intent(this, PublicPartiesActivity::class.java))
        }

        // Logout
        binding.btnLogout?.setOnClickListener {
            sessionManager.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
