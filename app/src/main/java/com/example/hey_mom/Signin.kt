package com.example.hey_mom
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.databinding.ActivitySigninBinding


class Signin : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySigninBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        binding.loginButton.setOnClickListener {
            val loginUsername = binding.username.text.toString().trim()
            val loginPassword = binding.password.text.toString().trim()

            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginDatabase(loginUsername, loginPassword)
        }
        binding.create.setOnClickListener{
            val intent = Intent(this,Signup::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun loginDatabase(username: String,password: String){
        val userExists = databaseHelper.readUser(username, password)
        if(userExists){
            Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,Homepage::class.java)
            startActivity(intent)
            finish()
        }else{
            Log.e("Database", "Login Failed: User not found for $username")
            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()
        }
    }
}
