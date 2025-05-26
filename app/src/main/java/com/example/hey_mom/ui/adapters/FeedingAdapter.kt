package com.example.hey_mom.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.FeedingEntry

class FeedingAdapter(
    private val items: List<FeedingEntry>,
    private val onEdit: (FeedingEntry) -> Unit
) : RecyclerView.Adapter<FeedingAdapter.FeedingViewHolder>() {

    inner class FeedingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.tvFeedingTime)
        val type: TextView = itemView.findViewById(R.id.tvFeedingType)
        val quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val notes: TextView = itemView.findViewById(R.id.tvFeedingNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feeding, parent, false)
        return FeedingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedingViewHolder, position: Int) {
        val entry = items[position]
        holder.time.text = "Time: ${entry.feeding_time}"
        holder.type.text = "Type: ${entry.feeding_type_notes}"
        holder.quantity.text = "Quantity: ${entry.quantity_ml} ml"
        holder.notes.text = "Notes: ${entry.notes ?: "None"}"

        holder.itemView.setOnClickListener { onEdit(entry) }
    }

    override fun getItemCount() = items.size
}

