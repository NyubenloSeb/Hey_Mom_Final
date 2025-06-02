package com.example.hey_mom.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.R
import com.example.hey_mom.api.models.Baby
import com.google.android.material.card.MaterialCardView

class ProfileAdapter(
    private val babies: List<Baby>,
    private val onClick: (Baby) -> Unit
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.profileIcon)
        val profileCard: MaterialCardView = itemView.findViewById(R.id.profileCard)

        fun bind(baby: Baby) {
            // Generate initials from baby name
            name.text = baby.name

            // Set click listener on the card
            profileCard.setOnClickListener {
                onClick(baby)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_icon, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(babies[position])
    }

    override fun getItemCount() = babies.size
}