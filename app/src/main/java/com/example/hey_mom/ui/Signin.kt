package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hey_mom.viewmodel.AuthViewModel
import com.example.hey_mom.R
import com.example.hey_mom.utils.SessionManager

class Signin : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, Homepage::class.java))
            finish()
            return
        }

        val emailInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val signupBtn = findViewById<TextView>(R.id.create)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                // Save user session using SessionManager
                sessionManager.saveUserSession(
                    userId = user.user_id.toString(),
                    userName = user.name,
                    userEmail = user.email,
                    user.contact_info ?: ""
                )

                Toast.makeText(this, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Homepage::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })

        signupBtn.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
            finish()
        }
    }
}