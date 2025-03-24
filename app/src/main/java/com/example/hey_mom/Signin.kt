package com.example.hey_mom
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hey_mom.databinding.ActivitySigninBinding


class Signin : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var button: Button
    lateinit var create: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySigninBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        button=findViewById(R.id.login_button)
        create=findViewById(R.id.create)

        button.setOnClickListener{
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }
        create.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }


    }
}
