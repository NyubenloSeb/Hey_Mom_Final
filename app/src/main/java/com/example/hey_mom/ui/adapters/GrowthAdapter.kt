package com.example.hey_mom.ui.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.GrowthEntry

class GrowthAdapter(private val items: List<GrowthEntry>) :
    RecyclerView.Adapter<GrowthAdapter.GrowthViewHolder>() {

    inner class GrowthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tvGrowthDate)
        val weight: TextView = itemView.findViewById(R.id.tvWeight)
        val height: TextView = itemView.findViewById(R.id.tvHeight)
        val head: TextView = itemView.findViewById(R.id.tvHeadCircumference)
        val notes: TextView = itemView.findViewById(R.id.tvGrowthNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrowthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_growth, parent, false)
        return GrowthViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrowthViewHolder, position: Int) {
        val entry = items[position]
        holder.date.text = "${entry.recorded_on}"
        holder.weight.text = "${entry.weight_kg} kg"
        holder.height.text = "${entry.height_cm} cm"
        holder.head.text = "${entry.head_circumference_cm} cm"
        holder.notes.text = "${entry.notes}"
    }

    override fun getItemCount() = items.size
}
