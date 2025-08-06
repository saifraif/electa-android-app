package bd.electa.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bd.electa.app.R
import bd.electa.app.data.CharterClause // Corrected import

class CharterAdapter(private val clauses: List<CharterClause>) :
    RecyclerView.Adapter<CharterAdapter.CharterViewHolder>() {

    class CharterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvClauseNumberAndGroup: TextView = itemView.findViewById(R.id.tvClauseNumberAndGroup)
        val tvClauseTitle: TextView = itemView.findViewById(R.id.tvClauseTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_charter_clause, parent, false)
        return CharterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clauses.size
    }

    override fun onBindViewHolder(holder: CharterViewHolder, position: Int) {
        val clause = clauses[position]
        holder.tvClauseTitle.text = clause.title
        holder.tvClauseNumberAndGroup.text = "Clause ${clause.clauseNumber} (${clause.clauseGroup})"
    }
}