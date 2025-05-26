package com.example.hey_mom.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.Baby

class BabyAdapter(
    private val babies: List<Baby>,
    private val onClick: (Baby) -> Unit
) : RecyclerView.Adapter<BabyAdapter.BabyViewHolder>() {

    inner class BabyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvBabyName)
        val dob: TextView = itemView.findViewById(R.id.tvBabyDob)
        val gender: TextView = itemView.findViewById(R.id.tvBabyGender)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_baby, parent, false)
        return BabyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        val baby = babies[position]
        holder.name.text = "Name: ${baby.name}"
        holder.dob.text = "DOB: ${baby.dob}"
        holder.gender.text = "Gender: ${baby.gender}"
        holder.itemView.setOnClickListener { onClick(baby) }
    }

    override fun getItemCount() = babies.size
}

