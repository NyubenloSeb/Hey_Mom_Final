package com.example.hey_mom.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hey_mom.viewmodel.AuthViewModel
import com.example.hey_mom.R

class Signin : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val emailInput = findViewById<EditText>(R.id.username)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val SignupBtn = findViewById<TextView>(R.id.create)

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
                // ✅ Save user_id in SharedPreferences
                val prefs = getSharedPreferences("HeyMomPrefs", MODE_PRIVATE)
                prefs.edit().putString("user_id", user.user_id.toString()).apply()

                Toast.makeText(this, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Homepage::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        })
        SignupBtn.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
            finish()
        }
    }
}
