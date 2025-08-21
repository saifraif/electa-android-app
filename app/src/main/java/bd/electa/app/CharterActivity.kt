package bd.electa.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import bd.electa.app.adapters.CharterClauseAdapter
import bd.electa.app.databinding.ActivityCharterBinding
import bd.electa.app.networking.RetrofitClient
import kotlinx.coroutines.launch

class CharterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharterBinding
    private lateinit var adapter: CharterClauseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CharterClauseAdapter(emptyList())
        binding.rvCharterClauses.layoutManager = LinearLayoutManager(this)
        binding.rvCharterClauses.adapter = adapter

        fetchClauses()
    }

    private fun fetchClauses() {
        showLoading(true)
        binding.tvEmptyState.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.getCharterClauses()
                if (resp.isSuccessful) {
                    val list = resp.body().orEmpty()
                    if (list.isNotEmpty()) {
                        adapter.updateData(list)
                        binding.rvCharterClauses.visibility = View.VISIBLE
                        binding.tvEmptyState.visibility = View.GONE
                    } else {
                        showEmpty()
                    }
                } else {
                    showError("Server error: ${resp.code()}")
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

    private fun showEmpty() {
        binding.rvCharterClauses.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    private fun showError(msg: String) {
        showEmpty()
        binding.tvEmptyState.text = msg
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
