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

        // Example wiring (use ?. to avoid crashes if IDs differ in your layout)
        binding.btnOpenCharter?.setOnClickListener {
            startActivity(Intent(this, CharterActivity::class.java))
        }

        binding.btnOpenEkyc?.setOnClickListener {
            startActivity(Intent(this, EkycActivity::class.java))
        }

        binding.btnOpenLogin?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnLogout?.setOnClickListener {
            sessionManager.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
