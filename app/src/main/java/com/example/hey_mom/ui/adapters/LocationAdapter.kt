package com.example.hey_mom.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hey_mom.api.models.LocationService
import com.example.hey_mom.databinding.ItemLocationBinding

class LocationAdapter(
    private val onItemClick: (LocationService) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val locations = mutableListOf<LocationService>()

    inner class LocationViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationService) {
            binding.tvLocName.text = location.loc_name
            binding.tvAddress.text = location.address
            binding.tvContact.text = location.contact_info
            binding.root.setOnClickListener {
                onItemClick(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    fun submitList(newList: List<LocationService>) {
        locations.clear()
        locations.addAll(newList)
        notifyDataSetChanged()
    }
}
