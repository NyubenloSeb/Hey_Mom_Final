package com.example.hey_mom

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.Calendar


class ChildDetailsFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_child_details, container, false)

        // References to UI components
        val tvSelectBirthdate: TextView = view.findViewById(R.id.tvSelectBirthdate)
        val tvSelectedBirthdate: TextView = view.findViewById(R.id.tvSelectedBirthdate)

        // Set a click listener for the TextView
        tvSelectBirthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog when TextView is clicked
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    tvSelectedBirthdate.text = selectedDate  // Display selected date in TextView
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        return view
    }
}