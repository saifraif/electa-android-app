package bd.electa.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import bd.electa.app.adapters.CharterClauseAdapter
import bd.electa.app.databinding.ActivityCharterBinding
import bd.electa.app.models.CharterClause
import bd.electa.app.networking.RetrofitClient
import kotlinx.coroutines.launch

class CharterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharterBinding
    private lateinit var adapter: CharterClauseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        fetchClauses()
    }

    private fun setupRecycler() {
        adapter = CharterClauseAdapter(onItemClick = { /* handle click if needed */ })
        binding.rvCharterClauses.layoutManager = LinearLayoutManager(this)
        binding.rvCharterClauses.adapter = adapter
        binding.rvCharterClauses.setHasFixedSize(true)
    }

    private fun fetchClauses() {
        showLoading(true)
        binding.tvEmptyState.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getCharterClauses()
                if (response.isSuccessful) {
                    val list: List<CharterClause> = response.body().orEmpty()
                    if (list.isNotEmpty()) {
                        adapter.submitList(list)
                        binding.rvCharterClauses.visibility = View.VISIBLE
                        binding.tvEmptyState.visibility = View.GONE
                    } else {
                        showEmptyState()
                    }
                } else {
                    showError("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("Network error: ${e.localizedMessage}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBarCharter.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvEmptyState.text = message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showEmptyState() {
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.rvCharterClauses.visibility = View.GONE
    }
}
