package com.example.hey_mom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CardFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        return when (position % 3) {
            0 -> WeatherFragment()
            1 -> TemperatureFragment()
            2 -> AirFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
