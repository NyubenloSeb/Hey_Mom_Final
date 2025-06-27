package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.hey_mom.R
import com.example.hey_mom.utils.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        // Initialize views
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)

        // Optional: Get clickable cards for navigation
        val editProfileCard = view.findViewById<MaterialCardView?>(R.id.editProfileCard)
        val settingsCard = view.findViewById<MaterialCardView?>(R.id.settingsCard)
        val helpSupportCard = view.findViewById<MaterialCardView?>(R.id.helpSupportCard)

        // Load user data from session
        loadUserData(tvName, tvEmail)

        // Setup logout button
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        // Setup click listeners for profile options (optional)
        editProfileCard?.setOnClickListener {
            // Navigate to edit profile
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        settingsCard?.setOnClickListener {
            // Navigate to settings
            // startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        helpSupportCard?.setOnClickListener {
            // Navigate to help & support
            // startActivity(Intent(requireContext(), HelpSupportActivity::class.java))
        }

        return view
    }

    private fun loadUserData(tvName: TextView, tvEmail: TextView) {
        val userName = sessionManager.getUserName()
        val userEmail = sessionManager.getUserEmail()

        tvName.text = userName ?: "User Name"
        tvEmail.text = userEmail ?: "user@example.com"
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        // Clear user session
        sessionManager.clearSession()

        // Navigate to signin screen and clear the activity stack
        val intent = Intent(requireContext(), Signin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // Finish the parent activity if needed
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        // Check if user is still logged in when fragment resumes
        if (!sessionManager.isLoggedIn()) {
            // Session expired, redirect to login
            val intent = Intent(requireContext(), Signin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}