package bd.electa.app

import android.os.Bundle
import android.view.View
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
        // --- ADJUSTMENT IMPLEMENTED ---
        binding.progressBarCharter.visibility = View.VISIBLE // 1. Show progress bar
        binding.rvCharterClauses.visibility = View.GONE     // 2. Hide the list
        // --- END ADJUSTMENT ---

        lifecycleScope.launch {
            try {
                // This will fail with 404, which is expected for now
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
            } finally {
                // --- ADJUSTMENT IMPLEMENTED ---
                binding.progressBarCharter.visibility = View.GONE // 3. Hide progress bar
                binding.rvCharterClauses.visibility = View.VISIBLE    // 4. Show the list
                // --- END ADJUSTMENT ---
            }
        }
    }
}