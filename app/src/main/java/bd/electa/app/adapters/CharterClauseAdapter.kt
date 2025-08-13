package bd.electa.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bd.electa.app.databinding.ItemCharterClauseBinding
import bd.electa.app.models.CharterClause

class CharterClauseAdapter(
    private var items: List<CharterClause>,
    private val onClick: (CharterClause) -> Unit = {}
) : RecyclerView.Adapter<CharterClauseAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCharterClauseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharterClauseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clause = items[position]
        holder.binding.tvClauseNumberAndGroup.text = "${clause.number} — ${clause.group}"
        holder.binding.tvClauseTitle.text = clause.title
        holder.binding.tvClauseDescription.text = clause.description
        holder.itemView.setOnClickListener { onClick(clause) }
    }

    fun updateData(newItems: List<CharterClause>) {
        items = newItems
        notifyDataSetChanged()
    }
}
