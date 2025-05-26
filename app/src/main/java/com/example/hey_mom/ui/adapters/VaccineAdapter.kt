package com.example.hey_mom.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.BabyVaccineStatus

class VaccineAdapter(
    private val items: List<BabyVaccineStatus>,
    private val onMarkCompleted: (BabyVaccineStatus) -> Unit
) : RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>() {

    inner class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvVaccineName)
        val status: TextView = itemView.findViewById(R.id.tvVaccineStatus)
        val due: TextView = itemView.findViewById(R.id.tvDueDate)
        val btnDone: Button = itemView.findViewById(R.id.btnMarkDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaccine, parent, false)
        return VaccineViewHolder(view)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.vaccine_name
        holder.status.text = "Status: ${item.status}"
        holder.due.text = "Recommended Age: ${item.recommended_age_weeks} weeks"
        holder.btnDone.visibility = if (item.status == "Pending") View.VISIBLE else View.GONE

        holder.btnDone.setOnClickListener { onMarkCompleted(item) }
    }

    override fun getItemCount() = items.size
}
