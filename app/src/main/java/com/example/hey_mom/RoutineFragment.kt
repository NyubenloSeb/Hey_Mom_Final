package com.example.hey_mom

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TimePicker
import androidx.core.content.ContextCompat


class RoutineFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_routine, container, false)

        val spinner: Spinner = view.findViewById(R.id.Spinner)
        val childspinner:Spinner=view.findViewById(R.id.child_spinner)
        val timePicker: TimePicker = view.findViewById(R.id.timePicker)
        val layoutContainer: View = view.findViewById(R.id.layout)

        // Get items from the string array
        val items = resources.getStringArray(R.array.routine_items)
        val childname=resources.getStringArray(R.array.kids_name)
        // Create an adapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val kidadapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, childname)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter to Spinner
        spinner.adapter = adapter
        childspinner.adapter=kidadapter
        // Listen for dropdown selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    1 -> setTime(timePicker, 6, 30)  // Option 1: Set time to 6:30 AM
                    2 -> setTime(timePicker, 12, 0)  // Option 2: Set time to 12:00 PM
                    3 -> setTime(timePicker, 18, 45) // Option 3: Set time to 6:45 PM
                }
                when (position) {
                    0 -> layoutContainer.setBackgroundColor(Color.TRANSPARENT)
                    1 -> layoutContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_blue))  // Light blue for every 2 hours
                    2 -> layoutContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_green))  // Light green for every 3 hours
                    3 -> layoutContainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_yellow))  // Light yellow for every 4 hours
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        return view
    }

    private fun setTime(timePicker: TimePicker, hour: Int, minute: Int) {
        timePicker.hour = hour
        timePicker.minute = minute
    }
}




