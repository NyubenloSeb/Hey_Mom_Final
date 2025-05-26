package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hey_mom.R
import com.example.hey_mom.viewmodel.AuthViewModel

class Signup : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameInput = findViewById<EditText>(R.id.name)
        val emailInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val passwordConfirmInput = findViewById<EditText>(R.id.password_confirm)
        val registerButton = findViewById<Button>(R.id.signup_button)
        val loginLink = findViewById<TextView>(R.id.backlogin)

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = passwordConfirmInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password, "Parent", null)
        }

        viewModel.registrationStatus.observe(this, Observer { success ->
            if (success == true) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Signin::class.java))
                finish()
            } else if (success == false) {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        })

        loginLink.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
            finish()
        }
    }
}



