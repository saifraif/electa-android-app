package bd.electa.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bd.electa.app.databinding.ActivityMainBinding
import bd.electa.app.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // If your layout has buttons, wire them; otherwise just keep it simple.
        // Example:
        // binding.btnGoLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
        // binding.btnGoCharter.setOnClickListener { startActivity(Intent(this, CharterActivity::class.java)) }
        // binding.btnGoEkyc.setOnClickListener { startActivity(Intent(this, EkycActivity::class.java)) }
    }
}
