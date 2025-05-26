package com.example.hey_mom.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.SleepEntry

class SleepAdapter(
    private val items: List<SleepEntry>,
    private val onEdit: (SleepEntry) -> Unit
) : RecyclerView.Adapter<SleepAdapter.SleepViewHolder>() {

    inner class SleepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val start: TextView = itemView.findViewById(R.id.tvSleepStart)
        val end: TextView = itemView.findViewById(R.id.tvSleepEnd)
        val notes: TextView = itemView.findViewById(R.id.tvSleepNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sleep, parent, false)
        return SleepViewHolder(view)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val entry = items[position]
        holder.start.text = "Start: ${entry.sleep_start}"
        holder.end.text = "End: ${entry.sleep_end}"
        holder.notes.text = "Notes: ${entry.notes ?: ""}"

        holder.itemView.setOnClickListener { onEdit(entry) }
    }

    override fun getItemCount() = items.size
}

