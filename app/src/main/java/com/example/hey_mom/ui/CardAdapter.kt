package com.example.hey_mom.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val layouts: List<Int>
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutId = layouts[viewType]
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        // You can bind data here if needed
    }

    override fun getItemCount(): Int = Int.MAX_VALUE // for infinite scroll illusion

    override fun getItemViewType(position: Int): Int {
        return position % layouts.size
    }
}
