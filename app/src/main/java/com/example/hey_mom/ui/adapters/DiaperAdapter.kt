package com.example.hey_mom.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.DiaperChange

class DiaperAdapter(
    private val items: List<DiaperChange>,
    private val onEdit: (DiaperChange) -> Unit
) : RecyclerView.Adapter<DiaperAdapter.DiaperViewHolder>() {

    inner class DiaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.tvChangeTime)
        val condition: TextView = itemView.findViewById(R.id.tvCondition)
        val notes: TextView = itemView.findViewById(R.id.tvDiaperNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diaper, parent, false)
        return DiaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaperViewHolder, position: Int) {
        val entry = items[position]
        holder.time.text = " ${entry.change_time}"
        holder.condition.text = "${entry.condition}"
        holder.notes.text = "${entry.notes ?: ""}"

        holder.itemView.setOnClickListener { onEdit(entry) }
    }

    override fun getItemCount() = items.size
}


