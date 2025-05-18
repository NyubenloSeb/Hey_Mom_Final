package com.example.hey_mom

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView

class ProfileFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        //profile options
        val listView = view.findViewById<ListView>(R.id.listViewProfile)

        val ProfileList = listOf(
            ProfileItem("Edit Profile", R.drawable.account,R.drawable.right),
            ProfileItem("Settings", R.drawable.setting,R.drawable.right),
            ProfileItem("Logout", R.drawable.logout,R.drawable.right)
        )

        val adapter = ProfileListAdapter(requireContext(), ProfileList)
        listView.adapter = adapter


        return view
    }
}
