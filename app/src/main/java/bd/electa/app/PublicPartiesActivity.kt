package bd.electa.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bd.electa.app.networking.ApiService
import bd.electa.app.networking.Party
import bd.electa.app.networking.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublicPartiesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listView = ListView(this)
        setContentView(listView)

        loadParties()
    }

    private fun loadParties() {
        uiScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.getParties(page = 1, size = 50)
                }
                if (resp.isSuccessful) {
                    val items: List<Party> = resp.body()?.items ?: emptyList()
                    val names = items.map { it.name + (it.abbrev?.let { ab -> " ($ab)" } ?: "") }
                    val adapter = ArrayAdapter(this@PublicPartiesActivity, android.R.layout.simple_list_item_1, names)
                    listView.adapter = adapter
                } else {
                    Toast.makeText(this@PublicPartiesActivity, "HTTP " + resp.code(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PublicPartiesActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
