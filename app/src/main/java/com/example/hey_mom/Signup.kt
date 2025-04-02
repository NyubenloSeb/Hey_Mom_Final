package com.example.hey_mom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        binding.backlogin.setOnClickListener{
            val intent = Intent(this,Signin::class.java)
            startActivity(intent)
            finish()
        }

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.username.text.toString().trim()
            val signupPassword = binding.password.text.toString().trim()
            val confirmPassword = binding.passwordConfirm.text.toString().trim()

            if (signupUsername.isEmpty() || signupPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (signupPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signupDatabase(signupUsername, signupPassword)
        }

    }

    private fun signupDatabase(username: String, password: String) {
        val insertedRowId = databaseHelper.insertUser(username, password)
        if (insertedRowId != -1L) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.e("Database", "Signup Failed: Could not insert into DB")
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }
}


