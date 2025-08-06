package bd.electa.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bd.electa.app.adapters.CharterAdapter
import bd.electa.app.databinding.ActivityCharterBinding
import bd.electa.app.networking.RetrofitClient
import kotlinx.coroutines.launch

class CharterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCharterClauses()
    }

    private fun fetchCharterClauses() {
        lifecycleScope.launch {
            try {
                // NOTE: The backend doesn't have this public endpoint yet.
                // This code will compile, but will fail at runtime with a 404 error.
                // This is expected and completes the UI part of the sprint.
                val response = RetrofitClient.instance.getCharterClauses()
                if (response.isSuccessful) {
                    val clauses = response.body()
                    if (!clauses.isNullOrEmpty()) {
                        binding.rvCharterClauses.adapter = CharterAdapter(clauses)
                    } else {
                        Toast.makeText(this@CharterActivity, "No clauses found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CharterActivity, "Error fetching data: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CharterActivity, "Network Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}