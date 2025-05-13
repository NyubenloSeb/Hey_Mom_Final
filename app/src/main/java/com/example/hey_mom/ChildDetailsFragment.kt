package com.example.hey_mom

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import java.util.Calendar


class ChildDetailsFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_child_details, container, false)

        val listView = view.findViewById<ListView>(R.id.listView)

        val childList = listOf(
            ChildItem("Add Child Details", R.drawable.bottle),
            ChildItem("View Children", R.drawable.bottle),
            ChildItem("Edit Children Details", R.drawable.bottle)
        )

        val adapter = ListAdapter(requireContext(), childList)
        listView.adapter = adapter



        return view
    }
}