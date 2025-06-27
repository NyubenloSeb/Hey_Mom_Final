package com.example.hey_mom.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.api.ApiClient
import com.example.hey_mom.api.ApiService
import com.example.hey_mom.repository.UserRepository
import com.example.hey_mom.utils.SessionManager
import com.example.hey_mom.viewmodel.UserViewModel
import com.example.hey_mom.viewmodel.UserViewModelFactory
import com.example.hey_mom.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etContact: EditText
    private lateinit var btnSave: Button

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(ApiClient.retrofit.create(ApiService::class.java)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sessionManager = SessionManager(this)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etContact = findViewById(R.id.etContact)
        btnSave = findViewById(R.id.btnSave)

        // Pre-fill data
        etName.setText(sessionManager.getUserName())
        etEmail.setText(sessionManager.getUserEmail())
        etContact.setText(sessionManager.getUserContact())

        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val contact = etContact.text.toString()

        btnSave.setOnClickListener {

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = sessionManager.getUserId()

            viewModel.updateProfile(userId, name, email, contact)
        }

        viewModel.status.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            if (status.contains("success", true)) {
                sessionManager.saveUser(name, email, contact)
                finish() // Close activity after update
            }
        }
    }
}
