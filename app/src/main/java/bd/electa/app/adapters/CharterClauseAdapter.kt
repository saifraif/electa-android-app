package bd.electa.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bd.electa.app.databinding.ItemCharterClauseBinding
import bd.electa.app.models.CharterClause

class CharterClauseAdapter(
    private val onItemClick: (CharterClause) -> Unit = {}
) : ListAdapter<CharterClause, CharterClauseAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CharterClause>() {
            override fun areItemsTheSame(oldItem: CharterClause, newItem: CharterClause): Boolean =
                oldItem.id == newItem.id && oldItem.number == newItem.number

            override fun areContentsTheSame(oldItem: CharterClause, newItem: CharterClause): Boolean =
                oldItem == newItem
        }
    }

    inner class VH(val binding: ItemCharterClauseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCharterClauseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.clause = item // relies on <variable name="clause" .../> in layout
        holder.binding.executePendingBindings()
        holder.binding.root.setOnClickListener { onItemClick(item) }
    }
}
